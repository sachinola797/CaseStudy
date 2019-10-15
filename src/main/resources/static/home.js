function select_option(category) {
    let select=document.getElementById("search-select");
    select.innerHTML+="<option name='category' value=''"+category+"'>"+category+"</option>";
}

function isAuthenticated(){
    const token=localStorage.getItem("Authentication");
    if(token==null)
        document.getElementById("myAccount").style.display="none";
    else
        document.getElementById("myAccount").style.display="block";
}

function isAuthenticated2(elment){
    const token=localStorage.getItem("Authentication");
    if(token==null)
        elment.style.display="none";
    else
        elment.style.display="block";
}

function d(element){
    console.log(element.display);
    element.style.display="none";
    console.log(element.display);
}

async function uploadData() {
    let response= await fetch("http://localhost:8080/products/allCategories",{method:"GET",});
    const body=await response.json();
    isAuthenticated();
    for (let i=0;i<body.length;i++)
    {
        select_option(body[i].name);
    }
}



function important() {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/cart/3/changeQuantity/2', true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');xhr.setRequestHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWNoaW4iLCJleHAiOjE1NzA5OTE3MTF9.rURGZ52q3Gk96hxS1HINSM5iUxRxFZS8h6Q_eVcGWWY-30F7TGBuGZ8QZdUYmMPYIoFNhcMV0Ub8JbeYT7cOng");
    xhr.onload = function () {
        // do something to response
        console.log(this.responseText);
    };
    xhr.send("quantity=2");

}







