package com.b2mark.kyc;


import com.b2mark.kyc.entity.KycJpaRepository;
import com.b2mark.kyc.entity.Kycinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/{uid}")
 class KycRestController {

    private final KycJpaRepository kycJpaRepository;

    @Autowired
    KycRestController(KycJpaRepository kycJpaRepository) {
        this.kycJpaRepository = kycJpaRepository;
    }

    @GetMapping
    Collection<Kycinfo> readKyc(@PathVariable Integer uid) {
        this.validateUser(uid);

        return this.kycJpaRepository.findAll();
    }

    @PostMapping
    ResponseEntity<?> add(@PathVariable Integer uid, @RequestBody Kycinfo input) {
        this.validateUser(uid);

        return this.kycJpaRepository.findByUid(uid)
                .map(account -> {
                    Kycinfo result = kycJpaRepository.save(new Kycinfo());

                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();
                    return ResponseEntity.created(location).build();
                })
                .orElse(ResponseEntity.noContent().build());

    }

    @GetMapping("/{bookmarkId}")
    Bookmark readBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
        this.validateUser(userId);

        return this.bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkNotFoundException(bookmarkId));
    }

    private void validateUser(Integer uId) {
        this.accountRepository.findByUsername(uId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }
}