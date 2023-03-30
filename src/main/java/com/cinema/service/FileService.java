package com.cinema.service;

import com.cinema.model.File;
import com.cinema.model.FileDto;
import net.jcip.annotations.ThreadSafe;

import java.util.Optional;

@ThreadSafe
public interface FileService {

    File save(FileDto fileDto);

    Optional<FileDto> getFileById(int id);

    boolean deleteById(int id);

}