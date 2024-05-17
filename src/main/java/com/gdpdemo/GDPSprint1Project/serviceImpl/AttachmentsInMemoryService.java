package com.gdpdemo.GDPSprint1Project.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gdpdemo.GDPSprint1Project.Attachments;
import com.gdpdemo.GDPSprint1Project.Posts;
import com.gdpdemo.GDPSprint1Project.Repository.AttachmentsRepository;
import com.gdpdemo.GDPSprint1Project.Repository.PostsRepository;
import com.gdpdemo.GDPSprint1Project.service.AttachmentsManager;

import javax.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@DependsOn("postsInMemoryService")
public class AttachmentsInMemoryService implements AttachmentsManager {

	private final List<Attachments> attachmentsList;
	private final AttachmentsRepository attachmentsRepository;
	private final PostsRepository postsRepository;

	@Autowired
	public AttachmentsInMemoryService(List<Attachments> attachmentsList, AttachmentsRepository attachmentsRepository,
			PostsRepository postsRepository) {
		this.attachmentsList = attachmentsList;
		this.attachmentsRepository = attachmentsRepository;
		this.postsRepository = postsRepository;
	}
	 // Implementation to save attachments related to a post
	@Override
	public void save(MultipartFile files, int id_post) {
			if (files.getOriginalFilename() != null && !files.getOriginalFilename().equals("")) {
				Attachments attachment = new Attachments();
				attachment.setId_post(id_post);
				attachment.setFilename(files.getOriginalFilename());
				attachment.setPosts(postsRepository.findById(attachment.getId_post()).get());
				attachmentsRepository.save(attachment);
			}
	}

	@Override
	public void saveAll() {
		attachmentsRepository.saveAll(attachmentsList);
	}

	@Override
	public List<Attachments> getAllAttachments() {
		return attachmentsRepository.findAll();
	}

	@Override
	public void remove(int id) {
		
		attachmentsRepository.deleteById(id);
	}

	
	@Override
	public void removeByPostId(int id) {
		
		attachmentsRepository.deleteAllByPostsId(id);
		
	}

	@Override
	public String getAttachmentFilename(int id) {
		return attachmentsRepository.findById(id).get().getFilename();
	}
	 // Method to retrieve attachments count per post
	@Override
	public Map<Integer, Integer> getAttachmentsWithAmount() {
		// Logic to gather counts of attachments per post
		List<Integer> integerList = new ArrayList<Integer>();
		for (Attachments attachments : attachmentsList) {
			integerList.add(attachments.getId_post());
		}
		for (Posts post : postsRepository.findAll()) {
			integerList.add(post.getId());
		}
		Map<Integer, Integer> countMap = new HashMap<>();

		for (Integer item : integerList) {

			if (countMap.containsKey(item))
				countMap.put(item, countMap.get(item) + 1);
			else
				countMap.put(item, 1);
		}
		return countMap;
	}

	public void savePosts() {
		for (Attachments a : attachmentsList) {
			a.setPosts(postsRepository.findById(a.getId_post()).get());
		}
	}
	 // Method executed after bean initialization to save posts and attachments
	@PostConstruct
	public void init() {
		savePosts();
		saveAll();
	}
	// Bean declaration to retrieve attachments list
	@Bean
	public List<Attachments> getAttachmentsList() {
		return attachmentsList;
	}

}
