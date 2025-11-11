package com.Kilari.Controller;

import com.Kilari.modal.Product;
import com.Kilari.modal.Review;
import com.Kilari.modal.User;
import com.Kilari.request.CreateReviewRequest;
import com.Kilari.response.ApiResponse;
import com.Kilari.services.ProductService;
import com.Kilari.services.ReviewService;
import com.Kilari.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);

    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> writeReview(@RequestBody CreateReviewRequest req, @PathVariable Long productId,@RequestHeader("Authorization")String jwt) throws Exception {
        User user  = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);
        Review review = reviewService.createReview(req,user,product);
        return ResponseEntity.ok(review);
    }


    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Review review = reviewService.updateReview(reviewId,req.getReviewText(),req.getReviewRating(),user.getId());
        return ResponseEntity.ok(review);
    }



    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId,
                                                    @RequestHeader("Authorization")String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId,user.getId());
        ApiResponse res= new ApiResponse();
        res.setMessage("Review deleted");
        return ResponseEntity.ok(res);
    }

}
