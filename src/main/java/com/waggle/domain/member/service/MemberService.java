package com.waggle.domain.member.service;

import com.waggle.domain.member.Member;
import com.waggle.domain.member.dto.UpdatePositionRequest;
import com.waggle.domain.member.repository.MemberRepository;
import com.waggle.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<Member> getProjectMembers(UUID projectId) {
        return memberRepository.findByProjectIdWithRelations(projectId);
    }

    @Transactional
    public Member updateMemberPosition(
        Long memberId,
        UpdatePositionRequest updatePositionRequest,
        User user
    ) {
        Member member = memberRepository.findByIdWithRelations(memberId)
            .orElseThrow(
                () -> new EntityNotFoundException("Member not found with id: " + memberId)
            );

        boolean isLeader = member.getProject().getLeader().getId().equals(user.getId());
        if (!isLeader) {
            throw new AccessDeniedException("Access denied to member with id: " + memberId);
        }

        // TODO: 해당 직무 공석 여부 확인

        member.updatePosition(updatePositionRequest.position());

        return member;
    }

    @Transactional
    public void deleteMember(Long memberId, User user) {
        Member member = memberRepository.findByIdWithRelations(memberId)
            .orElseThrow(
                () -> new EntityNotFoundException("Member not found with id: " + memberId)
            );

        boolean isOwner = member.getUser().getId().equals(user.getId());
        boolean isLeader = member.getProject().getLeader().getId().equals(user.getId());
        if (!(isOwner || isLeader)) {
            throw new AccessDeniedException("Access denied to member with id: " + memberId);
        }
        // TODO: isOwner && isLeader -> 리더 위임

        member.delete();
    }
}
