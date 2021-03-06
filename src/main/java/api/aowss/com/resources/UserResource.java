package api.aowss.com.resources;

import api.aowss.com.activities.CreateUser;
import api.aowss.com.activities.RetrieveUser;
import api.aowss.com.model.AccountStatus;
import api.aowss.com.representations.UserSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Inject
    CreateUser createUserActivity;

    @Inject
    RetrieveUser retrieveUserActivity;

    public UserResource() {
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Object>> register(@RequestBody @Valid UserSummary user) {
        return createUserActivity.
            createUser(user).
            thenApply(id -> {
                try {
                    return ResponseEntity.created(new URI("/user/" + id)).build();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    return ResponseEntity.created(null).build();
                }
            });
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Object>> retrieve(@PathVariable("userId") @NotNull Long userId) {
        return retrieveUserActivity.
            retrieveUser(userId).
            thenApply(userRepresentation -> ResponseEntity.ok(userRepresentation));
    }

    @RequestMapping(path = "/{userId}?status={status}&token={tokenId}", method = RequestMethod.PUT)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Object>> changeStatus(@PathVariable("userId") @NotNull Long userId, @RequestParam("status") @NotNull AccountStatus status, @RequestParam("tokenId") @NotNull String tokenId) {
        return retrieveUserActivity.
                retrieveUser(userId).
                thenApply(userRepresentation -> ResponseEntity.ok(userRepresentation));
    }

}