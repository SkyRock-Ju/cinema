package com.cinema.repository;

import com.cinema.model.File;
import net.jcip.annotations.ThreadSafe;

import java.util.Optional;

@ThreadSafe
public interface FileRepository {

    File save(File file);

    Optional<File> findById(int id);

    boolean deleteById(int id);
}
