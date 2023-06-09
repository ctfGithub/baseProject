package com.springbootbasepackage.service;

import com.springbootbasepackage.dto.ActivityImportWholesaleDto;
import com.springbootbasepackage.dto.PartnerSourceDTO;
import com.springbootbasepackage.dto.PartnerSourceDeleteDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface PartnerSourceService {

    List<PartnerSourceDTO> testPartnerSource();

    void importExcel(InputStream inputStream, Integer channelType);

    Integer save(PartnerSourceDTO partnerSourceDTO);

    String testRedis();

    Integer update(PartnerSourceDTO partnerSourceDTO);

    Integer delete(PartnerSourceDeleteDTO partnerSourceDTO);

    ActivityImportWholesaleDto wholesalerList(MultipartFile file);
}
