package com.shoppingmall.cashshop.controller;

import com.shoppingmall.cashshop.dto.ItemFormDto;
import com.shoppingmall.cashshop.dto.ItemSearchDto;
import com.shoppingmall.cashshop.entity.Item;
import com.shoppingmall.cashshop.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Log
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록 페이지
    @GetMapping(value = "/admin/item/new")
    public String itemForm(ItemFormDto itemFormDto, Model model) {

        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    // 상품 등록
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam(name = "itemImgFile") List<MultipartFile> itemImgFileList) {
        if(bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if(itemImgFileList.getFirst().isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage",
                    "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러 발생" + e.getMessage());
            return "item/itemForm";
        }

        return "redirect:/";
    }

    //상품 수정 페이지
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDetail(@PathVariable(name = "itemId") Long itemId, Model model) {

        try{
            ItemFormDto itemFormDto = itemService.getItem(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
        }
        return "item/itemForm";
    }

    //상품 수정
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@PathVariable Long itemId, @Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult, Model model,
                             @RequestParam(name = "itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다");
            return "item/itemForm";
        }

        // itemId와 itemFormDto.getId()가 일치하는지 검증
        if (!itemId.equals(itemFormDto.getId())) {
            model.addAttribute("errorMessage", "요청 경로와 상품 정보가 일치하지 않습니다.");
            return "item/itemForm";
        }

        try {
            log.info("ItemController's updateItem, itemImgFileList: " + itemFormDto.getItemImgIdList() + " " + itemImgFileList);
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다. " + e.getMessage());
            return "item/itemForm";
        }
        return "redirect:/";
    }



    // 상품 관리 페이지
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable("page")Optional<Integer> page,
                             Model model) {

        Pageable pageable = PageRequest.of(page.orElse(0), 8);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", items.getTotalPages());
        return "item/itemMng";
    }

    // 상품 상세 페이지
    @GetMapping(value = "/item/{itemId}")
    public String itemDetail(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItem(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDetail";
    }
}
