package com.b2mark.kyc;


import com.b2mark.kyc.entity.KycJpaRepository;
import com.b2mark.kyc.entity.Kycinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{uid}")
 class KycRestController {

    private final KycJpaRepository kycJpaRepository;

    @Autowired
    BookmarkRestController(KycJpaRepository kycJpaRepository) {
        this.kycJpaRepository = kycJpaRepository;
    }

    @GetMapping
    List<Kycinfo> readBookmarks(@PathVariable Integer uid) {
        this.validateUser(uid);

        return this.kycJpaRepository.findByUid(uid);
    }

    @PostMapping
    ResponseEntity<?> add(@PathVariable Integer uid, @RequestBody Kycinfo input) {
        this.validateUser(uid);

        return this.kycJpaRepository.findByUid(uid)
                .map(account -> {
                    Bookmark result = bookmarkRepository.save(new Bookmark(account,
                            input.getUri(), input.getDescription()));

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

    private void validateUser(Integer userId) {
        this.accountRepository.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }
}