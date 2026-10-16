package com.mohamedbenzraidi.proconnect.opportunity;

import com.mohamedbenzraidi.proconnect.common.exception.ApiException;
import com.mohamedbenzraidi.proconnect.opportunity.dto.CreateOpportunityRequest;
import com.mohamedbenzraidi.proconnect.opportunity.dto.OpportunityDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;

    public OpportunityServiceImpl(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDto> findAll() {
        return opportunityRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(OpportunityDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public OpportunityDto create(CreateOpportunityRequest request) {
        Opportunity opportunity = new Opportunity(
                request.title().trim(),
                request.organization().trim(),
                request.description().trim(),
                request.location().trim(),
                request.employmentType().trim(),
                request.requiredSkills() != null ? request.requiredSkills().trim() : null
        );

        Opportunity saved = opportunityRepository.save(opportunity);
        return OpportunityDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public void apply(UUID opportunityId, String userEmail) {
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Opportunité introuvable"));

        // Logique de candidature (ex: enregistrer dans une table d'applications si nécessaire)
    }
}