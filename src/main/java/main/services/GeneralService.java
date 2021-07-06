package main.services;

import main.Util.TagToDTOMapper;
import main.api.response.TagsResponse;
import main.model.DTO.TagDTO;
import main.model.Post;
import main.model.Tag;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GeneralService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    private final TagToDTOMapper tagToDTOMapper;

    public GeneralService(TagRepository tagRepository,
                          PostRepository postRepository,
                          TagToDTOMapper tagToDTOMapper) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.tagToDTOMapper = tagToDTOMapper;
    }

    public ResponseEntity<TagsResponse> getTags(String query) {
        List<Tag> tags = tagRepository.getTags();

        List<TagDTO> tagDTOs = new ArrayList<>();
        tags.forEach(tag -> tagDTOs.add(tagToDTOMapper.convertToDTO(tag)));

        double postCount = postRepository.getAllPosts(LocalDateTime.now()).size();

        tagDTOs.forEach(tagDTO -> tagDTO.setWeight(tagRepository.countTagsByName(tagDTO.getName()) / postCount));

        //подсчитаем коэффициент по наибольшему значению
        tagDTOs.sort((tag1, tag2) -> tag2.getWeight().compareTo(tag1.getWeight()));
        double k = 1.0/tagDTOs.get(0).getWeight();

        tagDTOs.forEach(tagDTO -> tagDTO.setWeight(tagDTO.getWeight() / k));

        TagsResponse response = new TagsResponse();
        response.setTags(tagDTOs);

        return ResponseEntity.ok(response);
    }


}
