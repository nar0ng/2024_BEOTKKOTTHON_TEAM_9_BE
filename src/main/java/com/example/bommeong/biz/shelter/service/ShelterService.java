package com.example.bommeong.biz.shelter.service;

import com.example.bommeong.biz.post.repository.PostRepository;
import com.example.bommeong.biz.shelter.dto.BomListDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterService {
    private final PostRepository postRepository;

    public List<BomListDto> findAllBomListByShelterId(Long shelterId){
        return postRepository.findByShelterId_Id(shelterId).stream()
                .map(post -> new BomListDto(post.getPostId(), post.getBomInfoEntity().getName(), post.getBomInfoEntity().getBreed(),
                        post.getBomInfoEntity().getGender(), post.getBomInfoEntity().getExtra()))
                .collect(Collectors.toList());
    }
}
