package org.lesley.ecommerce.email;

import lombok.Getter;

public enum EmailTemplate {
    PAYMENT_CONFIRMATION("payment-confimation.html","Payement successfully processed"),
    ORDER_CONFIRMATION("order-confimation.html","Order confirmation");


    @Getter
            private final  String template;
            @Getter
            private final String subject;

    EmailTemplate(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
