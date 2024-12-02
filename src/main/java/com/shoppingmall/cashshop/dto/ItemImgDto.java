package com.shoppingmall.cashshop.dto;

import com.shoppingmall.cashshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {
    private long id;

    private String imgName; //이미지 파일명

    private String originalImgName; //원본 이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
