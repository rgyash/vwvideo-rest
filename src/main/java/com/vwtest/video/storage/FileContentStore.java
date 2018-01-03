package com.vwtest.video.storage;

import org.springframework.content.commons.repository.ContentStore;

import com.vwtest.video.domain.File;

public interface FileContentStore extends ContentStore<File, String> {

}