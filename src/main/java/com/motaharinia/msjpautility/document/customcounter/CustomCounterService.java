package com.motaharinia.msjpautility.document.customcounter;



import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * کلاس پیاده سازی سرویس تولیدPrimaryKey
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomCounterService{

    private final CustomCounterRepository customCounterRepository;

    public CustomCounterService(CustomCounterRepository customCounterRepository) {
        this.customCounterRepository = customCounterRepository;
    }

    /**
     * متد تولید شناسه جدید برای یک کالکشن مانگو
     * @param documentName نام کالکشن موردنظر
     * @return خروجی: شناسه جدید
     */
    @NotNull
    public Long generatePrimaryKey(@NotNull String documentName) {
        Optional<CustomCounterDocument> customDocumentOptional = customCounterRepository.findById(documentName);
        if (customDocumentOptional.isPresent()) {
            CustomCounterDocument customCounterDocument = customDocumentOptional.get();
            customCounterDocument.setPrimaryKey(customCounterDocument.getPrimaryKey()+1);
            customCounterRepository.save(customCounterDocument);
            return customCounterDocument.getPrimaryKey();
        }else {
            return customCounterRepository.save(new CustomCounterDocument(documentName,1L)).getPrimaryKey();
        }
    }
}
