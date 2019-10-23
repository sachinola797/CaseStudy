if (role == null || token == null || role !== "USER" || userId == null)
    window.location = "/loginPage";

function getOrderList() {
    let header = new Header("Authorization", token);
    sendRequest("/order/" + userId + "/getOrders", "GET", [header], null, setOrder);
}

let setOrder = function() {
    if (this.status === 401 || this.getResponseHeader("tokenValidity") === "expired"){
        localStorage.removeItem("Authorization");
        localStorage.removeItem("userId");
        localStorage.removeItem("role");
        window.location = "/loginPage";
    }
    if (this.status === 400) {
        document.getElementById("Orders").innerHTML += "<div class='alert alert-danger'><strong>" + this.responseText + "</strong><br></div>";
    }

    if (this.status === 200) {
        let orders = JSON.parse(this.response);

        let orderlist = document.getElementById("Orders");

        for (let i = 0; i < orders.length; i++) {
            let totalPrice=0;
            let orderlayer = document.getElementById("order-layer-template").cloneNode(true);
            orderlayer.style.display = "block";
            orderlayer.id=orders[i].orderId;
            orderlayer.children[0].children[0].innerText += orders[i].orderId;
            orderlayer.children[0].children[1].innerText += orders[i].orderStatus;

            for (let j = 0; j < orders[i].orderItems.length; j++) {
                let orderItem = orders[i].orderItems[j];
                let product = document.getElementById("order-body-template").cloneNode(true);
                product.id=orders[i].orderId+"_"+orderItem.product.productId;
                product.style.display = "block";
                product.children[0].children[0].children[0].innerText = orderItem.product.name;
                product.children[0].children[0].children[1].innerText = orderItem.product.details;
                product.children[0].children[1].innerText += orderItem.product.price;
                product.children[0].children[2].innerText = orderItem.quantity;
                //product.children[1].children[0].children[0].innerHTML = orderItem.product.productId;

                totalPrice += orderItem.product.price * orderItem.quantity;
                orderlayer.appendChild(product);
            }
            let orderFooter = document.getElementById("order-footer-template").cloneNode(true);
            orderFooter.style.display="flex";
            orderFooter.children[0].children[0].innerHTML += totalPrice;
            orderlayer.appendChild(orderFooter);
            orderlist.appendChild(orderlayer);
        }
    }
};
