package com.shoppingmall.cashshop.service;

import com.shoppingmall.cashshop.dto.ItemFormDto;
import com.shoppingmall.cashshop.dto.ItemImgDto;
import com.shoppingmall.cashshop.dto.ItemSearchDto;
import com.shoppingmall.cashshop.dto.MainItemDto;
import com.shoppingmall.cashshop.entity.Item;
import com.shoppingmall.cashshop.entity.ItemImg;
import com.shoppingmall.cashshop.repository.ItemImgRepository;
import com.shoppingmall.cashshop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log
@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        if (itemImgFileList == null || itemImgFileList.isEmpty()) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다.");
        }

        for(int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            itemImg.setRepImgYn(i == 0 ? "Y" : "N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItem(Long itemId) {

        // 상품 id 기반으로 상품 이미지 엔티티 객체 로드
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        if (itemImgList.isEmpty()) {
            throw new EntityNotFoundException("이미지가 존재하지 않습니다.");
        }

        // 상품 이미지 DTO 객체를 담을 그릇 생성
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        //이미지 엔티티 객체 -> 상품 이미지 DTO
        for(ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품 id 기반으로 상품 엔티티 객체 로드
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        //상품 엔티티 객체 -> DTO
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws IOException {
        // 아이템 조회
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        // 이미지 ID 리스트 가져오기
        List<Long> itemImgIds = itemFormDto.getItemImgIdList();

        for (int i = 0; i < itemImgFileList.size(); i++) {
            try {
                itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
            } catch (Exception e) {
                throw new RuntimeException("상품 이미지 수정 중 에러 발생 ItemService.updateItemImg");
            }
        }

        return item.getId();

    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
