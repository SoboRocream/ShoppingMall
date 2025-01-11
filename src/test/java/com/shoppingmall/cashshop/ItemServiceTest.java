package com.shoppingmall.cashshop;

import com.shoppingmall.cashshop.constant.ItemSellStatus;
import com.shoppingmall.cashshop.dto.ItemFormDto;
import com.shoppingmall.cashshop.entity.Item;
import com.shoppingmall.cashshop.entity.ItemImg;
import com.shoppingmall.cashshop.repository.ItemImgRepository;
import com.shoppingmall.cashshop.repository.ItemRepository;
import com.shoppingmall.cashshop.service.ItemService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository imgRepository;
    @Autowired
    private ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFiles = new ArrayList<>();

        for(int i=0; i<5;i++){
            String path = "/Users/kbg46/Desktop/Cashshop/ItemResource";
            String imageName = "image"+i+".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFiles.add(multipartFile);
        }
        return multipartFiles;
    }

    @Test
    @DisplayName("상품 등록 서비스")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemName("TEST ITEM");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("TEST ITEM DES");
        itemFormDto.setPrice(10000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFiles = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFiles);
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityExistsException::new);

        assertEquals(itemFormDto.getItemName(), item.getItemName());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFiles.get(0).getOriginalFilename(), itemImgList.get(0).getOriginalImgName());
    }
}
