const token = localStorage.getItem("Authorization");
const userId = localStorage.getItem("userId");
const role = localStorage.getItem("role");
if (role == null || token == null || role === "ADMIN" || userId == null)
    window.location = "loginPage.html";

function getCartList() {
    let header = new Header("Authorization", token);
    sendRequest("http://localhost:8080/cart/" + userId + "/getCart", "GET", [header], null, setCart);
}
let req6;

let setCart = function() {
    req6 = this;
    if (this.status === 401 || this.getResponseHeader("tokenValidity") === "expired")
        window.location = "loginPage.html";
    if (this.status === 400) {
        document.getElementById("Cart").innerHTML += "<div class='alert alert-danger'><strong>" + this.responseText + "</strong><br></div>";
        document.getElementById("place-Order").style.display = "none";
    }

    if (this.status === 200) {
        let cartData = JSON.parse(this.response);
        if(cartData.cartItems.length==0){
            document.getElementById("Cart").innerHTML += "<div class='alert alert-danger'><strong>Your cart is Empty!!!</strong><br></div>";
            document.getElementById("place-Order").style.display = "none";
            return;
        }

        let cartList = document.getElementById("Cart");
        let cartInfo = document.getElementById("cart-item-info-template").cloneNode(true);
        cartInfo.style.display = "flex";
        cartList.appendChild(cartInfo);
        let totalPrice = 0;
        let cartItemData;
        for (cartItemData of cartData.cartItems) {
            let cartItem = document.getElementById("cart-item-template").cloneNode(true);
            cartItem.style.display = "flex";
            cartItem.children[0].children[0].innerText = cartItemData.product.name;
            cartItem.children[0].children[1].innerText = cartItemData.product.details;
            cartItem.children[1].innerText += cartItemData.product.price;
            cartItem.children[2].children[0].children[0].innerText = cartItemData.product.productId;
            cartItem.children[2].children[0].children[1].value = cartItemData.quantity;
            cartItem.children[2].children[1].children[0].innerText = cartItemData.product.productId;
            cartItem.children[3].innerText += cartItemData.quantity * cartItemData.product.price;
            totalPrice += cartItemData.quantity * cartItemData.product.price;
            cartList.appendChild(cartItem);
        }

        let orderDetails = document.getElementById("place-Order");
        orderDetails.children[1].children[1].innerText = cartData.cartItems.length;
        orderDetails.children[2].children[1].children[0].innerText += totalPrice;

    }
};

let req7;
function updateProduct(Obj,event) {
    event.preventDefault();
    const productId=Obj.children[0].innerHTML;
    const quantity=Obj.children[1].value;
    console.log(productId);
    console.log(quantity);
    // if(!Number(productId) || !Number(quantity+.1)) //adding .1 to the quantity bcz if quantity=0 ,then it will try to go inside the loop
    //     return;
    const data= new FormData();
    data.append("quantity",quantity);
    let header=new Header("Authorization",token);
    sendRequest("http://localhost:8080/cart/"+userId+"/changeQuantity/"+productId, "POST", [header], data, function() {
        req7=this;
        if (this.status===401 || this.getResponseHeader("tokenValidity") === "expired")
            window.location = "loginPage.html";
        if(this.status===400) {
            let message=document.getElementById("message");
            message.classList.add("show");
            message.children[1].innerHTML=this.responseText;
        }
        if(this.status===200){
            if(quantity==0)
                alert(this.responseText);
            else
                alert(JSON.parse(this.responseText).product.name+" updated successfully!!!");
            window.location="cartPage.html";
        }
    });
    return false;
}

function removeItem(Obj) {
    const productId=Obj.children[0].innerText;
    if(!Number(productId))
        return;
    let header = new Header("Authorization", token);
    sendRequest("http://localhost:8080/cart/"+userId+"/remove/"+productId, "GET", [header], null, function() {
        req3=this;
        if (this.status===401 || this.status===400 || this.getResponseHeader("tokenValidity") === "expired")
            window.location = "loginPage.html";
        if(this.status===200){
            alert(this.responseText);
            window.location="cartPage.html";
        }
    });
}

function placeOrder() {
    let header = new Header("Authorization", token);
    sendRequest("http://localhost:8080/order/"+userId+"/createOrder", "GET", [header], null, function() {
        req3=this;
        if (this.status===401 || this.status===400 || this.getResponseHeader("tokenValidity") === "expired")
            window.location = "loginPage.html";
        if(this.status===200){
            alert(JSON.parse(this.responseText).orderStatus);
            window.location="homePage.html";
        }
    });
}

