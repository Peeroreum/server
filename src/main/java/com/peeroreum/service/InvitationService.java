package com.peeroreum.service;

import com.peeroreum.domain.Invitation;
import com.peeroreum.domain.Wedu;
import com.peeroreum.repository.InvitationRepository;
import com.peeroreum.service.attachment.ImageService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final ImageService imageService;

    public InvitationService(InvitationRepository invitationRepository, ImageService imageService) {
        this.invitationRepository = invitationRepository;
        this.imageService = imageService;
    }

    void deleteInvitation(Wedu wedu) {
        Invitation invitation = invitationRepository.findByWedu(wedu);
        imageService.deleteImage(invitation.getImage().getId());
        invitationRepository.delete(invitation);
    }
}
