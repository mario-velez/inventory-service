package com.acelerati.management_service.domain.api;

import com.acelerati.management_service.domain.model.CartModel;

public interface CartServicePort {
    CartModel getCartByUserId(Long idUser);
}
