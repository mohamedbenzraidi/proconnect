package com.mohamedbenzraidi.proconnect.opportunity;

import com.mohamedbenzraidi.proconnect.opportunity.dto.CreateOpportunityRequest;
import com.mohamedbenzraidi.proconnect.opportunity.dto.OpportunityDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @GetMapping
    public ResponseEntity<List<OpportunityDto>> getAllOpportunities() {
        return ResponseEntity.ok(opportunityService.findAll());
    }

    @PostMapping
    public ResponseEntity<OpportunityDto> createOpportunity(@RequestBody CreateOpportunityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(opportunityService.create(request));
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<Void> apply(@PathVariable UUID id, Principal principal) {
        opportunityService.apply(id, principal.getName());
        return ResponseEntity.ok().build();
    }
}