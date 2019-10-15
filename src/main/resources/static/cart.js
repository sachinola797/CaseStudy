let setCart = function() {
    if (this.status != 200)
        return;
    let cartData = JSON.parse(this.response);
    let cart = document.getElementById("cart");
    let cartInfo = document.getElementById("cart-item-info-template").cloneNode(true);
    cartInfo.style.display = "block";
    cart.appendChild(cartInfo);
    for (let i = 0; i < cartData.cartItems.length; i++) {
        let cartItemData = cartData.cartItems[i];
        let cartItem = document.getElementById("cart-item-template").cloneNode(true);
        cartItem.style.display = "block";
        cartItem.children[0].children[0].innerText = cartItemData.product.name;
        cartItem.children[0].children[1].innerText = cartItemData.product.details;
        cartItem.children[1].innerText += cartItemData.product.price;
        cartItem.children[2].children[0].value = cartItemData.quantity;
        cartItem.children[3].innerText += cartItemData.quantity * cartItemData.product.price;
        cart.appendChild(cartItem);
    }
};

function getItemList() {
    let header = [{ name: "Authorization", value: "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWNoaW4iLCJleHAiOjE1NzEwODU4MDZ9.rTyLvDeUEt-taL_jpiVxxuFz1Bnp89yaLLMGJ8-llaMfmskC-ju92cU_RDOI5g-GFAeRJs1myBlyhTPn-SAPiA" }];
    sendRequest("http://localhost:8080/cart/3/getCart", "GET", header, null, setCart);
}