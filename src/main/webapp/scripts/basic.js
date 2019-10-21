const token = localStorage.getItem("Authorization");
const userId = localStorage.getItem("userId");
const role = localStorage.getItem("role");

//-----------------Nav bar starts---------------------------
function navbar() {
    if (role === "ADMIN") {
        document.getElementById("navbar-addProduct").classList.remove("d-none");
        document.getElementById("navbar-modifyProduct").classList.remove("d-none");
        document.getElementById("login-button").classList.add("d-none");
        document.getElementById("logout-button").classList.remove("d-none");
        document.getElementById("customerSupport-button").classList.remove("d-none");
    }
    if (role === "USER") {
        document.getElementById("login-button").classList.add("d-none");
        document.getElementById("userName").classList.remove("d-none");
        document.getElementById("myCart").classList.remove("d-none");
        document.getElementById("logout-button").classList.remove("d-none");

    }
}


function logout(){
    let header=new Header("Authorization",token);
    sendRequest("http://localhost:8080/logout","GET",[header],null,function () {
        if(this.status===200){
            alert("You have been logout successfully...");
            localStorage.removeItem("Authorization");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");
            window.location="/";
        }
    })
}

//-------------------- Nav bar ends -------------------------


function sendRequest(url,method,header_list,body,callback) {
    let request=new XMLHttpRequest();
    request.open(method,url);
    if(header_list!=null) {
        for (let i = 0; i < header_list.length; i++)
            request.setRequestHeader(header_list[i].name, header_list[i].value);
    }
    request.onload= callback;
    if(body==null)
        request.send();
    else
        request.send(body);
}


function Header(name,value) {
    this.name=name;
    this.value=value;
}

function User(name,email,password) {
    this.name=name;
    this.password=password;
    this.email=email;
}

function addToCart(Obj) {
    const token = localStorage.getItem("Authorization");
    const userId = localStorage.getItem("userId");
    const role = localStorage.getItem("role");
    if (role == null || token == null || role !== "USER" || userId == null)
        window.location = "/loginPage";
    const productId=(Obj.parentElement.parentElement.id.split("_"))[1];
    if(!Number(productId))
        return;
    let header = new Header("Authorization", token);
    sendRequest("http://localhost:8080/cart/"+userId+"/add/"+productId, "GET", [header], null, function() {

        if (this.status===401 || this.getResponseHeader("tokenValidity") === "expired"){
            localStorage.removeItem("Authorization");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");
            window.location = "/loginPage";
        }
        if(this.status===200){
            alert(JSON.parse(this.responseText).product.name+" added to your cart successfully!!!");
        }
    });
}

function callingAddProductPage(){
    localStorage.removeItem("modifyProduct_Id");
    window.location="/addProductPage";

}

function callingModifyProductPage(){
    localStorage.setItem("modifyProduct_Id",1);
    window.location="/modifyProductPage";
}

function customFade(element,seconds) {
    setTimeout(function fade() {
        let op = 1;
        let timer = setInterval(function () {
            if (op <= 0.1){
                clearInterval(timer);
                element.style.display = 'none';
            }
            element.style.opacity = op;
            element.style.filter = 'alpha(opacity=' + op * 100 + ")";
            op -= op * 0.1;
        }, 50);
    },seconds*1000);
};