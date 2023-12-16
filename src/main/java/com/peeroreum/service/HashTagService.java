package com.peeroreum.service;

import com.peeroreum.domain.HashTag;
import com.peeroreum.domain.Wedu;
import com.peeroreum.repository.HashTagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HashTagService {
    private final HashTagRepository hashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    public Set<HashTag> createHashTags(Wedu wedu, List<String> hashTags) {
        Set<HashTag> hashTagSet = new HashSet<>();
        for(String tag : hashTags) {
            HashTag hashTag = new HashTag(tag, wedu);
            hashTagRepository.save(hashTag);
            hashTagSet.add(hashTag);
        }
        return hashTagSet;
    }
    public List<String> readHashTags(Wedu wedu) {
        List<HashTag> hashTags = hashTagRepository.getAllByWedu(wedu);
        List<String> tags = new ArrayList<>();
        for(HashTag hashTag : hashTags) {
            tags.add(hashTag.getTag());
        }
        return tags;
    }

    public Set<Wedu> searchHashTags(String keyword) {
        Set<HashTag> hashTags = new HashSet<>(hashTagRepository.findAllByTagContaining(keyword));
        Set<Wedu> wedus = hashTags.stream().map(HashTag::getWedu).collect(Collectors.toSet());
        return wedus;
    }

    public void deleteHashTags(Wedu wedu) {
        hashTagRepository.deleteAllByWedu(wedu);
    }

}
