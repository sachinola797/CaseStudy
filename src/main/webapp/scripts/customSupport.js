if (role == null || token == null || role !== "ADMIN" || userId == null)
    window.location = "/loginPage";

function customersearch(event) {
    event.preventDefault();
    document.getElementById("user-info").classList.add("d-none");
    document.getElementById("Orders").classList.add("d-none");
    const phone=document.getElementById("phone-search").value;
    let header = new Header("Authorization",token);
    sendRequest("http://localhost:8080/getProfileByPhone/" + phone, "GET", [header], null, insertCustomerDetails);

}

let insertCustomerDetails = function() {

    if (this.status === 401 || this.getResponseHeader("tokenValidity") === "expired"){
        localStorage.removeItem("Authorization");
        localStorage.removeItem("userId");
        localStorage.removeItem("role");
        window.location = "/loginPage";
    }
    if(this.status===400){
        let msg=document.getElementById("customer-error");
        msg.classList.remove("hide");
        msg.classList.add("show");
        msg.children[1].innerHTML=this.response;
    }

    if (this.status === 200) {
        let userData = JSON.parse(this.responseText);
        let userInfo = document.getElementById("user-info");
        userInfo.classList.remove("d-none");

        userInfo.children[0].children[1].innerHTML = userData.name;
        userInfo.children[1].children[1].innerHTML = userData.email;
        userInfo.children[2].children[1].innerHTML = userData.phone;
        userInfo.children[3].children[0].children[0].children[1].innerHTML = userData.address.street;
        userInfo.children[3].children[0].children[1].children[1].innerHTML = userData.address.city;
        userInfo.children[3].children[0].children[2].children[1].innerHTML = userData.address.state;
        userInfo.children[3].children[0].children[3].children[1].innerHTML = userData.address.pincode;

        let getOrders=document.getElementById("get_customer_order");
        getOrders.classList.remove("d-none");
        getOrders.children[0].id=userData.userID;
    }
};

function getCustomerOrders(Obj) {
    Obj.parentElement.classList.add("d-none");
    let header = new Header("Authorization", token);
    sendRequest("http://localhost:8080/order/" + Obj.id + "/getOrders", "GET", [header], null, function () {
        if (this.status === 401 || this.getResponseHeader("tokenValidity") === "expired") {
            localStorage.removeItem("Authorization");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");
            window.location = "/loginPage";
        }
        if (this.status === 400) {
            let orderElement=document.getElementById("Orders");
            orderElement.classList.remove("d-none");
            orderElement.innerHTML += "<div class='alert alert-danger'><strong>User doesn't have any placed orders...</strong><br></div>";
        }

        if (this.status === 200) {
            let orders = JSON.parse(this.response);

            let orderlist = document.getElementById("Orders");
            orderlist.classList.remove("d-none");
            for (let i = 0; i < orders.length; i++) {
                let totalPrice = 0;
                let orderlayer = document.getElementById("order-layer-template").cloneNode(true);
                orderlayer.style.display = "block";
                orderlayer.id = orders[i].orderId;
                orderlayer.children[0].children[0].innerText += orders[i].orderId;
                orderlayer.children[0].children[1].innerText += orders[i].orderStatus;

                for (let j = 0; j < orders[i].orderItems.length; j++) {
                    let orderItem = orders[i].orderItems[j];
                    let product = document.getElementById("order-body-template").cloneNode(true);
                    product.id = orders[i].orderId + "_" + orderItem.product.productId;
                    product.style.display = "block";
                    product.children[0].children[0].children[0].innerText = orderItem.product.name;
                    product.children[0].children[0].children[1].innerText = orderItem.product.details;
                    product.children[0].children[1].innerText += orderItem.product.price;
                    product.children[0].children[2].innerText = orderItem.quantity;

                    totalPrice += orderItem.product.price * orderItem.quantity;
                    orderlayer.appendChild(product);
                }
                let orderFooter = document.getElementById("order-footer-template").cloneNode(true);
                orderFooter.style.display = "flex";
                orderFooter.children[0].children[0].innerHTML += totalPrice;
                orderlayer.appendChild(orderFooter);
                orderlist.appendChild(orderlayer);
            }
        }
    });
}