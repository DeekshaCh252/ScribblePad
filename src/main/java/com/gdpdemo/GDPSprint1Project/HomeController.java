package com.gdpdemo.GDPSprint1Project;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.gdpdemo.GDPSprint1Project.serviceImpl.DemoServiceImp;

import com.gdpdemo.GDPSprint1Project.Repository.HomeRepository;
import com.gdpdemo.GDPSprint1Project.service.AttachmentsManager;
import com.gdpdemo.GDPSprint1Project.service.CommentsManager;
import com.gdpdemo.GDPSprint1Project.service.EmailService;
import com.gdpdemo.GDPSprint1Project.service.PostsAuthorsManager;
import com.gdpdemo.GDPSprint1Project.service.PostsManager;

import com.gdpdemo.GDPSprint1Project.storage.StorageService;

@Controller
public class HomeController {

	@Value("${upoadDir}")
	private String uploadFolder;

	@Autowired

	private static final Logger logger = Logger.getLogger(HomeController.class);
	/*
	 * { BasicConfigurator.configure(); }
	 */
	@Autowired
	private HomeRepository homeRepository;

	@Autowired
	private EmailService emailservice;

	@Autowired
	private PostsManager postsManager;

	@Autowired
	private PostsAuthorsManager postsAuthorsManager;

	@Autowired
	private StorageService storageService;

	@Autowired
	DemoServiceImp demoService;

	@Autowired
	AttachmentsManager attachmentsManager;

	ModelAndView mv = new ModelAndView();
	// Handler for the home page
	@RequestMapping("/")
	public String home() {
		logger.debug("Loading Scribblepad......");
		/*
		 * logger.info("INFO level message.."); logger.warn("WARN level message..!!");
		 * logger.error("ERROR level message..!!");
		 * logger.fatal("FATAL level message...!!");
		 * logger.trace("TRACE Level message..!!");
		 */
		logger.debug("Exit of Home");
		return "Home";
	}
	// Handler for creating posts
	@RequestMapping("/createpost")
	public String createPost() {
		logger.debug("User is trying to Create a post");
		logger.debug("Exit from Creating a Post");
		return "createpost";
	}
	// Handler for signing in
	@RequestMapping("/SignIn")
	public String SignIn(Model model) {
		logger.debug("User is trying to Register");
		model.addAttribute("user", new Home());
		logger.debug("Exit from SignUp");
		return "SignIn";
	}
	// Handler for categories
	@RequestMapping("/category")
	public String category(HttpSession session,Model model) {
		String email = (String) session.getAttribute("email");
		System.out.println(email);
		boolean disableForm = true;
		if(email.contains("nwmissouri.edu")) {
			disableForm = false;
		}
		model.addAttribute("disableForm", disableForm);
		logger.debug("Categories");
		logger.debug("Exit from Categories");
		return "category";
	}

	@RequestMapping("/dummy")
	public String dummy() {
		return "dummy";
	}
//Handler for reported posts
	@RequestMapping("/reported")
	public String reported() {
		return "reported";
	}
//Handler for declined posts
	@RequestMapping("/declined")
	public String declined() {
		return "declined";
	}
//Handler for sending OTP
	@RequestMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email, HttpSession session) throws MessagingException {
		System.out.println("EMAIL" + email);
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		System.out.println(otp);
		emailservice.sendOtpMessage(email, otp);
		session.setAttribute("myotp", otp);
		session.setAttribute("email", email);
		return "verifyOtp";
	}
//HAndler for resetting and sending OTP
	@RequestMapping("/resetSendOtp")
	public String resetSendOtp(@RequestParam("email") String email, HttpSession session) throws MessagingException {
		System.out.println("EMAIL" + email);
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		System.out.println(otp);
		emailservice.sendOtpMessage(email, otp);
		session.setAttribute("myotp", otp);
		session.setAttribute("email", email);
		return "ResetverifyOtp";
	}
// Handler for handling sign up
	@RequestMapping("/SignUp")
	public String SignUp(Model model) {
		model.addAttribute("user", new Home());
		return "SignUp";
	}
	// Handler for handling forgot password
	@RequestMapping(value = "/ForgotPassword", method = RequestMethod.POST)
	public String registeredUser(@Validated @ModelAttribute("user") Home user, BindingResult result, Model model,
			HttpSession session) {
		try {
			if (!user.getPassword().equals(user.getRePassword())) {
				session.setAttribute("message", "Re-type your password correctly!!");
				throw new Exception("Re-type your password correctly!!");
			}

			if (result.hasErrors()) {
				System.out.println("Error" + result.toString());
				model.addAttribute("user", user);
				return "SignIn";
			}

			Home home = this.homeRepository.getUserByUserName(user.getEmail());
			if (home == null) {
				System.out.println("USER" + user);
				/* Home save = homeRepository.save(user) */;
				demoService.save(user);

				model.addAttribute("user", new Home());
				session.setAttribute("email", user.getEmail());
				return "ForgotPassword";
			}
			if (user.getEmail().equals(home.getEmail())) {
				session.setAttribute("message", "User with this email already exists");
				return "SignIn";
			}
			// session.setAttribute("message", new Message("Successfully Registered!!!",
			// "alert-success"));

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", e.getMessage());
			return "SignIn";
		}
		return "SignIn";
	}

	// Handler for forgot password
	@RequestMapping("/ForgotPassword")
	public String ForgotPassword() {
		return "ForgotPassword";
	}
	// Handler for resetting password
	@RequestMapping("/ResetPassword")
	public String ResetPassword() {
		return "ResetPassword";
	}
	// Handler for changing password
	@RequestMapping("/ChangePassword")
	public String ChangePassword() {
		return "ChangePassword";
	}
	// Handler for library category
	@GetMapping("/library/{page}")
	public String library(@RequestParam(name= "year", required = false, defaultValue = "2023") int year,
	        @RequestParam(name="month", required = false, defaultValue = "1") int month,@PathVariable("page") Integer page,Model model,HttpSession session) {
		// Logic for displaying posts in the library category
		String email = (String) session.getAttribute("email");
		System.out.println(email);
		boolean disableForm = true;
		if(email.contains("nwmissouri.edu")) {
			disableForm = false;
		}
		System.out.println(disableForm);
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); 
		Pageable pageable = PageRequest.of(page, 5,sort);
		Page<Posts> posts = this.postsManager.getAllPostsByFilter(-1, null, null, "library","APPROVED",year,month,pageable);
		//model.addAttribute("posts", postsManager.getAllPostsOrderedCategory(-1, null, null, "library","APPROVED"));
	    // Creating a new Page with updated posts
		if(posts.getContent() !=null) {
			posts = getImage(posts,pageable);
		}
		model.addAttribute("disableForm", disableForm);
		model.addAttribute("title", "My Post");
		model.addAttribute("posts", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", posts.getTotalPages());
		return "library";
	}

	// Handler for sports category
	@RequestMapping("/sports/{page}")
	public String sports(@RequestParam(name= "year", required = false, defaultValue = "2023") int year,
	        @RequestParam(name="month", required = false, defaultValue = "1") int month,@PathVariable("page") Integer page,Model model,HttpSession session) {
		String email = (String) session.getAttribute("email");
		System.out.println(email);
		boolean disableForm = true;
		if(email.contains("nwmissouri.edu")) {
			disableForm = false;
		}
		System.out.println(disableForm);
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); 
		Pageable pageable = PageRequest.of(page, 5,sort);
		Page<Posts> posts = this.postsManager.getAllPostsByFilter(-1, null, null, "sports","APPROVED",year,month,pageable);
		//model.addAttribute("posts", postsManager.getAllPostsOrderedCategory(-1, null, null, "sports","APPROVED"));
		if(posts.getContent() !=null) {
			posts = getImage(posts,pageable);
		}
		model.addAttribute("disableForm", disableForm);
		model.addAttribute("title", "My Post");
		model.addAttribute("posts", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", posts.getTotalPages());
		return "sports";
	}
	// Handler for cultural category
	@RequestMapping("/cultural/{page}")
	public String cultural(@RequestParam(name= "year", required = false, defaultValue = "2023") int year,
	        @RequestParam(name="month", required = false, defaultValue = "1") int month,@PathVariable("page") Integer page,Model model,HttpSession session) {
		String email = (String) session.getAttribute("email");
		System.out.println(email);
		boolean disableForm = true;
		if(email.contains("nwmissouri.edu")) {
			disableForm = false;
		}
		System.out.println(disableForm);
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); 
		Pageable pageable = PageRequest.of(page, 5,sort);
		Page<Posts> posts = this.postsManager.getAllPostsByFilter(-1, null, null, "culture","APPROVED",year,month,pageable);
		//model.addAttribute("posts", postsManager.getAllPostsOrderedCategory(-1, null, null, "culture","APPROVED"));
		if(posts.getContent() !=null) {
			posts = getImage(posts,pageable);
		}
		model.addAttribute("disableForm", disableForm);
		model.addAttribute("title", "My Post");
		model.addAttribute("posts", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", posts.getTotalPages());
		return "cultural";
	}

	/*
	 * @RequestMapping("/admin") public String admin() { return "admin"; }
	 */
	// Handler for miscellaneous category
	@RequestMapping("/misc/{page}")
	public String misc(@RequestParam(name= "year", required = false, defaultValue = "2023") int year,
	        @RequestParam(name="month", required = false, defaultValue = "1") int month,@PathVariable("page") Integer page,Model model,HttpSession session) {
		String email = (String) session.getAttribute("email");
		System.out.println(email);
		boolean disableForm = true;
		if(email.contains("nwmissouri.edu")) {
			disableForm = false;
		}
		System.out.println(disableForm);
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); 
		Pageable pageable = PageRequest.of(page, 5,sort);
		Page<Posts> posts = this.postsManager.getAllPostsByFilter(-1, null, null, "mis","APPROVED",year,month,pageable);
		//model.addAttribute("posts", postsManager.getAllPostsOrderedCategory(-1, null, null, "mis","APPROVED"));
		if(posts.getContent() !=null) {
			posts = getImage(posts,pageable);
		}
		model.addAttribute("disableForm", disableForm);
		model.addAttribute("title", "My Post");
		model.addAttribute("posts", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", posts.getTotalPages());
		return "misc";
	}
	// Handler for user login
	@RequestMapping("/Login")
	public String Login(Model model) {
		model.addAttribute("title", "Login Page");
		return "Login";
	}
	// Logic for displaying user-specific library posts page wise
	@GetMapping("/mylibraryposts/{page}")
	public String myLibraryPost(@RequestParam(name= "year", required = false, defaultValue = "2023") int year,
	        @RequestParam(name="month", required = false, defaultValue = "1") int month,@PathVariable("page") Integer page,Model model, HttpSession session) {
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); 
		Integer id = (int) session.getAttribute("id");
		
		Pageable pageable = PageRequest.of(page, 5,sort);
		Page<Posts> posts = this.postsAuthorsManager.getAllPostsByAuthors(id,"library",year,month,pageable);
		if(posts.getContent() !=null) {
			posts = getImage(posts,pageable);
		}
		model.addAttribute("title", "My Post");
		model.addAttribute("category","library");
		model.addAttribute("posts", posts );
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", posts.getTotalPages());
		return "mylibraryposts";

	}
	// Logic for displaying user-specific sports posts page wise
	@RequestMapping("/mysportsposts/{page}")
	public String mySportsPost(@RequestParam(name= "year", required = false, defaultValue = "2023") int year,
	        @RequestParam(name="month", required = false, defaultValue = "1") int month,@PathVariable("page") Integer page,Model model,HttpSession session) {
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); 
		Integer id = (int) session.getAttribute("id");
		Pageable pageable = PageRequest.of(page, 5,sort);
		Page<Posts> posts = this.postsAuthorsManager.getAllPostsByAuthors(id,"sports",year,month,pageable);
		//Page<Posts> posts = this.postsManager.getAllPostsPages(-1, null, null, "sports",year,month,id, pageable);
		if(posts.getContent() !=null) {
			posts = getImage(posts,pageable);
		}
		model.addAttribute("title", "My Post");
		model.addAttribute("category","sports");
		model.addAttribute("posts", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", posts.getTotalPages());
		return "mysportsposts";

	}
	// Logic for displaying user-specific cultural posts page wise
	@RequestMapping("/mycultureposts/{page}")
	public String myCulturePost(@RequestParam(name= "year", required = false, defaultValue = "2023") int year,
	        @RequestParam(name="month", required = false, defaultValue = "1") int month,@PathVariable("page") Integer page,Model model,HttpSession session) {
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); 
		Integer id = (int) session.getAttribute("id");
		Pageable pageable = PageRequest.of(page, 5,sort);
		Page<Posts> posts = this.postsAuthorsManager.getAllPostsByAuthors(id,"culture",year,month,pageable);
		//Page<Posts> posts = this.postsManager.getAllPostsPages(-1, null, null, "culture",year,month,id, pageable);
		if(posts.getContent() !=null) {
			posts = getImage(posts,pageable);
		}
		model.addAttribute("title", "My Post");
		model.addAttribute("category","cultural");
		model.addAttribute("posts", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", posts.getTotalPages());
		return "mycultureposts";

	}
	// Logic for displaying user-specific miscellaneous posts page wise
	@RequestMapping("/mymiscposts/{page}")
	public String myMiscPost(@RequestParam(name= "year", required = false, defaultValue = "2023") int year,
	        @RequestParam(name="month", required = false, defaultValue = "1") int month,@PathVariable("page") Integer page,Model model,HttpSession session) {
		
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); 
		Integer id = (int) session.getAttribute("id");
		Pageable pageable = PageRequest.of(page, 5,sort);
		Page<Posts> posts = this.postsAuthorsManager.getAllPostsByAuthors(id,"mis",year,month,pageable);
		//Page<Posts> posts = this.postsManager.getAllPostsPages(-1, null, null, "mis",year,month,id, pageable);
		if(posts.getContent() !=null) {
			posts = getImage(posts,pageable);
		}
		model.addAttribute("title", "My Post");
		model.addAttribute("category","misc");
		model.addAttribute("posts", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", posts.getTotalPages());
		return "mymiscposts";

	}

	/*
	 * @PostMapping("/add_comment") public String
	 * addComment(@RequestBody @ModelAttribute("comment") Comments comment,
	 * BindingResult br, int id_post, Model model) { if (br.hasErrors()) { return
	 * "home"; } // comment.setUsername(username); commentsManager.save(comment);
	 * return "redirect:/"; }
	 */
	//Handler for user login and verification
	@RequestMapping(value = "/category", method = RequestMethod.POST)
	public String loginUser(@Valid @ModelAttribute("user") Home user, BindingResult result, Model model,
			HttpSession session) {
		// Logic for user login and verification
		try {
			if (user.getEmail() == "" || user.getPassword() == "") {
				throw new Exception("Please Enter your Credentials!!");

			}
			/*
			 * if (result.hasErrors()) { System.out.println("Error" + result.toString());
			 * model.addAttribute("user", user); return "Login"; }
			 */
			System.out.println("USER" + user.getEmail());
			System.out.println("USER" + user.getId());
			/* System.out.println(emailId); */
			Home home = this.homeRepository.getUserByUserName(user.getEmail());
			if (home == null) {
				session.setAttribute("message", "User does not exits with this email !!");
				logger.error("User does not exits with this email !!");
				return "Login";
			} else {
				/*
				 * if (!home.getPassword().matches(user.getPassword())) {
				 * session.setAttribute("message", "Please Enter Correct Password");
				 * logger.error("Please Enter Correct Password"); return "Login";
				 */
				boolean check = demoService.retrieve(home, user.getPassword());
				if (!check) {
					session.setAttribute("message", "Please Enter Correct Password");
					logger.error("Please Enter Correct Password");
					return "Login";
				}
			}
			boolean disableForm = true;
			if(home.getEmail().contains("nwmissouri.edu")) {
				disableForm = false;
			}
			session.setAttribute("firstName", home.getFirstName());
			session.setAttribute("lastName", home.getLastName());
			session.setAttribute("fullName", home.fullName());
			session.setAttribute("email", home.getEmail());
			session.setAttribute("id", home.getId());
			model.addAttribute("disableForm", disableForm);
			logger.debug("User enters into website");
			System.out.println("USER" + home.getId());
			if(home.getEmail().contains("rushidhar@Admin")) {
				
				System.out.println("admin entered");
				return "admin";
			}
			   return "category";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "Login";
		}

	}
	//Handler for verifying OTP
	@RequestMapping(value = "/verify-otp", method = RequestMethod.POST)
	public String validateOtp(@RequestParam("otp") int otpnum, HttpSession session) {
		//Logic for verifying OTP
		Integer myOtp = (int) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");
		if (myOtp == otpnum) {
			Home home = this.homeRepository.getUserByUserName(email);
			session.setAttribute("message", new Message("You have Successfully Registered!!!", "alert-success"));
			return "Sucess";
		} else {
			session.setAttribute("message", "You have entered wrong otp");
			return "verifyOtp";
		}

	}
// Handler for resetting OTP validation
	@RequestMapping(value = "/resetverifyotp", method = RequestMethod.POST)
	public String resetValidateOtp(@RequestParam("otp") int otpnum, HttpSession session) {
		//Logic for resetting OTP validation
		Integer myOtp = (int) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");
		if (myOtp == otpnum) {
			Home home = this.homeRepository.getUserByUserName(email);
			if (home == null) {
				session.setAttribute("message", "User does not exits with this email !!");
				return "ResetPassword";
			} else {
				session.setAttribute("message", new Message("You have Successfully Registered!!!", "alert-success"));
			}
			return "password_change_form";
		} else {
			session.setAttribute("message", "You have entered wrong otp");
			return "ResetverifyOtp";
		}

	}
	// Handler for changing password
	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
		// Logic for changing password
		String email = (String) session.getAttribute("email");
		Home home = this.homeRepository.getUserByUserName(email);
		home.setPassword(newpassword);
		home.setRePassword(newpassword);
		this.homeRepository.save(home);
		session.setAttribute("message", new Message("Password Successfully Changed", "alert-success"));
		return "ResetSuccess";

	}
	// Handler for adding a new post
	@RequestMapping(value = "/add_post", method = RequestMethod.POST)
	public String addPost(@RequestBody @Valid @ModelAttribute("posts") Posts postss, BindingResult br,
			BindingResult bindingResultForId, @RequestParam("files") MultipartFile files, HttpSession session) throws IOException {
		// Logic for adding a new post
		if (br.hasErrors() || bindingResultForId.hasErrors()) {
			return "home";
		}
		String email = (String) session.getAttribute("email");
		Home home = this.homeRepository.getUserByUserName(email);
		postss.setAttachments(files.getBytes());
		postss.setContent(splitString(postss.getPost_content()));
		postss.setStatus("PENDING");
		postss.setDate(LocalDate.now());
		postsManager.save(postss);
		postsAuthorsManager.save(postss.getId(), home.getId());
		storageService.store(files);
		attachmentsManager.save(files, postss.getId());

		return "redirect:/category";
	}


	// Utility method to limit post content length
	public String splitString(String post_content) {
		// Logic to limit post content length
		String s = "";
		if (post_content.length() > 150) {
			s = post_content.substring(0, 150) + "....";
		}
		return s;
	}
	
	//Utility method to retrieve images for posts
	public Page<Posts> getImage(Page<Posts> posts, Pageable pageable) {
		// Logic to retrieve images for posts
		List<Posts> updatedPosts = new ArrayList<>();
			for (Posts post : posts.getContent()) {
		        if (post.getAttachments() != null) {
		            byte[] encodeBase64 = Base64.getEncoder().encode(post.getAttachments());
		            String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
		            post.setImage64(base64Encoded);
		        }
	
		        updatedPosts.add(post);
		    
		}
		return new PageImpl<>(updatedPosts, pageable, posts.getTotalElements());
	}

}

