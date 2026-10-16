package com.mohamedbenzraidi.proconnect.opportunity;

import com.mohamedbenzraidi.proconnect.opportunity.dto.CreateOpportunityRequest;
import com.mohamedbenzraidi.proconnect.opportunity.dto.OpportunityDto;

import java.util.List;
import java.util.UUID;

public interface OpportunityService {
    List<OpportunityDto> findAll();
    OpportunityDto create(CreateOpportunityRequest request);
    void apply(UUID opportunityId, String userEmail);
}