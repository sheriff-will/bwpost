package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.ReconcileRepository;
import com.application.iserv.ui.payments.models.ReconcileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReconcileService {
    
    private final ReconcileRepository reconcileRepository;

    @Autowired
    public ReconcileService(ReconcileRepository reconcileRepository) {
        this.reconcileRepository = reconcileRepository;
    }
    
    public void updateRemunerationDetails(String date, List<ReconcileModel> reconcileModels) {
        reconcileRepository.updateRemunerationDetails(date, reconcileModels);
    }
    
}
