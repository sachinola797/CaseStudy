if (role == null || token == null || role !== "USER" || userId == null)
    window.location = "/loginPage";

function getCartList() {
    let header = new Header("Authorization", token);
    sendRequest("http://localhost:8080/cart/" + userId + "/getCart", "GET", [header], null, setCart);
}

let setCart = function() {

    if (this.status === 401 || this.getResponseHeader("tokenValidity") === "expired"){
        localStorage.removeItem("Authorization");
        localStorage.removeItem("userId");
        localStorage.removeItem("role");
        window.location = "/loginPage";
    }
    if (this.status === 400) {
        document.getElementById("Cart").innerHTML += "<div class='alert alert-danger'><strong>" + this.responseText + "</strong><br></div>";
        document.getElementById("place-Order").style.display = "none";
    }

    if (this.status === 200) {
        let cartData = JSON.parse(this.response);
        if(cartData.cartItems.length===0){
            document.getElementById("Cart").innerHTML += "<div class='alert alert-danger'><strong>Your cart is Empty!!!</strong><br></div>";
            document.getElementById("place-Order").style.display = "none";
            return;
        }

        let cartList = document.getElementById("Cart");
        let cartInfo = document.getElementById("cart-item-info-template");
        cartInfo.style.display = "flex";
        cartList.appendChild(cartInfo);
        let totalPrice = 0;
        let cartItemData;
        for (cartItemData of cartData.cartItems) {
            let cartItem = document.getElementById("cart-item-template").cloneNode(true);
            cartItem.style.display = "flex";
            cartItem.id=cartItemData.product.productId;
            cartItem.children[0].children[0].innerText = cartItemData.product.name;
            cartItem.children[0].children[1].innerText = cartItemData.product.details;
            cartItem.children[1].innerText += cartItemData.product.price;
            cartItem.children[2].children[0].children[0].value = cartItemData.quantity;
            cartItem.children[3].innerText += cartItemData.quantity * cartItemData.product.price;
            totalPrice += cartItemData.quantity * cartItemData.product.price;
            cartList.appendChild(cartItem);
        }

        let orderDetails = document.getElementById("place-Order");
        orderDetails.children[1].children[1].innerText = cartData.cartItems.length;
        orderDetails.children[2].children[1].children[0].innerText += totalPrice;

    }
};


function updateProduct(Obj) {
    const productId=Obj.parentElement.parentElement.id;
    const quantity=Obj.children[0].value;
    if(quantity<1){
        let message=document.getElementById("message");
        message.classList.add("show");
        message.children[1].innerHTML="Please insert a valid quantity...";
        return;
    }

    const data= new FormData();
    data.append("quantity",quantity);
    let header=new Header("Authorization",token);
    sendRequest("http://localhost:8080/cart/"+userId+"/changeQuantity/"+productId, "POST", [header], data, function() {

        if (this.status===401 || this.getResponseHeader("tokenValidity") === "expired"){
            localStorage.removeItem("Authorization");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");
            window.location = "/loginPage";
        }
        if(this.status===400) {
            let message=document.getElementById("message");
            message.classList.add("show");
            message.children[1].innerHTML=this.responseText;
        }
        if(this.status===200){
            if(quantity===0)
                alert(this.responseText);
            window.location="/cartPage";
        }
    });
}

function removeItem(Obj) {
    const productId=Obj.parentElement.parentElement.parentElement.id;

    if(!Number(productId))
        return;
    let header = new Header("Authorization", token);
    sendRequest("http://localhost:8080/cart/"+userId+"/remove/"+productId, "GET", [header], null, function() {

        if (this.status===401 || this.status===400 || this.getResponseHeader("tokenValidity") === "expired"){
            localStorage.removeItem("Authorization");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");
            window.location = "/loginPage";
        }
        if(this.status===200){
            alert(this.responseText);
            window.location="/cartPage";
        }
    });
}

function placeOrder() {
    let header = new Header("Authorization", token);
    sendRequest("http://localhost:8080/order/"+userId+"/createOrder", "GET", [header], null, function() {

        if (this.status===401 || this.status===400 || this.getResponseHeader("tokenValidity") === "expired"){
            localStorage.removeItem("Authorization");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");
            window.location = "loginPage.html";
        }
        if(this.status===200){
            alert(JSON.parse(this.responseText).orderStatus);
            window.location="homePage.html";
        }
    });
}

