package com.gdpdemo.GDPSprint1Project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gdpdemo.GDPSprint1Project.Attachments;

import java.util.List;
import java.util.Map;

@Service
public interface AttachmentsManager {

	void save(MultipartFile files, int id_post);

	void saveAll();

	List<Attachments> getAllAttachments();

	void remove(int id);

	void removeByPostId(int id);

	String getAttachmentFilename(int id);

	Map<Integer, Integer> getAttachmentsWithAmount();
	

}
