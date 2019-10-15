var a;
function getOrderList() {
    let header=[{name:"Authorization",value:"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWNoaW4iLCJleHAiOjE1NzEwNzA3NTB9.GQYleuH7KpxmqDe57HAbzP7Lhi43WzoOkW5c3y9RAr4zgChgtRnn3Nw5QWUOasdWhhtVDAsWNLa7awY0bXJlJA"}];
    sendRequest("http://localhost:8080/order/3/getOrders","GET",header,null,setOrder);
}

let setOrder= function()  {
    if(this.status!=200)
        return;
     let orders=JSON.parse(this.response);
     a=orders;
     let orderlist=document.getElementById("Orders");
     for(let i=0;i<orders.length;i++) {
         let orderHead=document.getElementById("order-layer-template").cloneNode(true);
         orderHead.style.display="block";
         orderHead.children[0].children[0].innerText+=orders[i].orderId;
         orderHead.children[0].children[1].innerText+=orders[i].orderItems.length;
         orderHead.children[0].children[2].innerText+=orders[i].orderStatus;

         for(let j=0;j<orders[i].orderItems.length;j++){
             let orderItem=orders[i].orderItems[j];
             let product=document.getElementById("order-body-template").cloneNode(true);
             product.style.display="block";
             product.children[0].children[0].innerText=orderItem.product.name;
             product.children[0].children[1].innerText=orderItem.product.details;
             product.children[0].children[2].innerText=orderItem.product.price;
             product.children[0].children[3].innerText=orderItem.quantity;
             orderHead.appendChild(product);
         }
         orderlist.appendChild(orderHead);
     }
};

function sendRequest(url,method,header,body,callback) {
    let request=new XMLHttpRequest();
    request.open(method,url);
    if(header!=null) {
        for (let i = 0; i < header.length; i++)
            request.setRequestHeader(header[i].name, header[i].value);
    }
    request.onload= callback;
    request.send(body);
}
