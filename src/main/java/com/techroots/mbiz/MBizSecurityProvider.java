package com.techroots.mbiz;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import io.quarkus.amazon.lambda.http.LambdaIdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;

import javax.enterprise.context.ApplicationScoped;
import java.security.Principal;

@ApplicationScoped
public class MBizSecurityProvider implements LambdaIdentityProvider {

    @Override
    public SecurityIdentity authenticate(APIGatewayV2HTTPEvent event) {
        if (event.getHeaders() == null || !event.getHeaders().containsKey("x-user")) {
            throw new RuntimeException("No auth header");
        }
        if(!event.getHeaders().get("x-user").equalsIgnoreCase("test")) {
            throw new RuntimeException("Invalid user");
        }
        Principal principal = new QuarkusPrincipal(event.getHeaders().get("x-user"));
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder();
        builder.setPrincipal(principal);
        return builder.build();
    }
}
