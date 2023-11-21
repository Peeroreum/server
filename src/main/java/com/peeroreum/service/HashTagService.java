package com.peeroreum.service;

import com.peeroreum.domain.HashTag;
import com.peeroreum.domain.Wedu;
import com.peeroreum.repository.HashTagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HashTagService {
    private final HashTagRepository hashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    public void createHashTags(Wedu wedu, List<String> hashTags) {
        for(String tag : hashTags) {
            HashTag hashTag = new HashTag(tag, wedu);
            hashTagRepository.save(hashTag);
        }
    }
    public List<String> readHashTags(Wedu wedu) {
        List<HashTag> hashTags = hashTagRepository.getAllByWedu(wedu);
        List<String> tags = new ArrayList<>();
        for(HashTag hashTag : hashTags) {
            tags.add(hashTag.getTag());
        }
        return tags;
    }
    public void deleteHashTags(Wedu wedu) {
        hashTagRepository.deleteAllByWedu(wedu);
    }

}
