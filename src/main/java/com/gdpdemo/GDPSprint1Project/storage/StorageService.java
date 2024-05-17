package com.gdpdemo.GDPSprint1Project.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	void store(MultipartFile files);

	Stream<Path> loadAll();

}
