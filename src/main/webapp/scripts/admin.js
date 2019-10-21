let modifyProduct_Id=localStorage.getItem("modifyProduct_Id");
if (role == null || token == null || role !== "ADMIN" || userId == null)
    window.location = "/loginPage";

function Product(productId,name,price,details,category,subcategories) {
    this.productId=productId;
    this.name=name;
    this.price=price;
    this.details=details;
    this.category=category;
    this.subcategories=subcategories;
}


function subcategoriesAsList(subcategories) {
    let finalList=[];
    let list=subcategories.split(",");
    for(let i=0;i<list.length;i++){
        const a=list[i].trim();
        if(a!=="")
            finalList.push(a);
    }
    return finalList;
}

function submitProduct(event) {
    event.preventDefault();
    let subcategories=subcategoriesAsList(document.getElementById("subcategories").value);

    let product= new Product(document.getElementById("productId").value,document.getElementById("name").value,document.getElementById("price").value,
        document.getElementById("details").value,document.getElementById("category").value,subcategories);
    let header1 = new Header("Authorization", token);
    let header2 = new Header("Content-Type", "application/json;charset=UTF-8");
    let url;
    if(modifyProduct_Id!=null)
        url="http://localhost:8080/products/updateProduct";
    else
        url="http://localhost:8080/products/addProduct";

    sendRequest(url, "POST", [header1, header2], JSON.stringify(product), function() {

        let msg=document.getElementById("product-error");
        if (this.status===401 || this.getResponseHeader("tokenValidity") === "expired"){
            localStorage.removeItem("Authorization");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");
            window.location = "/loginPage";
        }
        if(this.status===400){
            msg.classList.add("alert-danger");
            msg.classList.add("show");
            msg.children[1].innerHTML=this.responseText;
        }
        if(this.status===200){
            msg.classList.remove("alert-danger");
            msg.classList.add("show");
            msg.classList.add("alert-success");
            let response_Product=JSON.parse(this.responseText);
            if(modifyProduct_Id!=null)
                msg.children[1].innerHTML="Your Product \""+response_Product.name+"\" has been updated successfully!!!";
            else
                msg.children[1].innerHTML="Your Product \""+response_Product.name+"\" has been added successfully!!!\nThe Product Id is "+JSON.parse(this.responseText).productId+".";
        }

    });
    return false;
}

function checkForModify() {
    if(modifyProduct_Id!=null){
        document.getElementById("title-for-modify").innerText="Please insert the Product Key of the product you want to modify!!!";
        document.getElementById("productForm").children[8].innerHTML="Modify Product";

        let productId_element=document.getElementById("product-for-modify");
        productId_element.classList.remove("d-none");
        if(Number(modifyProduct_Id)) {
            productId_element.children[1].value = modifyProduct_Id;
            setModifyForm(productId_element.children[1]);
        }
    }
}

function setModifyForm(productId_element) {
    const productId=productId_element.value;
    let msg=document.getElementById("product-error");
    if(!Number(productId) || modifyProduct_Id==null){
        msg.classList.add("alert-danger");
        msg.classList.add("show");
        msg.children[1].innerHTML="Please insert a valid product id...";
        return;
    }
    sendRequest("http://localhost:8080/products/getById/"+productId, "GET", null, null, function() {

        if (this.status===401 || this.getResponseHeader("tokenValidity") === "expired"){
            localStorage.removeItem("Authorization");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");
            window.location = "/loginPage";
        }
        if(this.status===400){
            msg.classList.add("alert-danger");
            msg.classList.add("show");
            msg.children[1].innerHTML=this.responseText;
        }
        if(this.status===200){
            const product=JSON.parse(this.responseText);
            document.getElementById("name").value=product.name;
            document.getElementById("price").value=product.price;
            document.getElementById("details").value=product.details;
            document.getElementById("category").value=product.category;
            let subcategory;
            let subcat_value="";
            for(subcategory of product.subcategories)
                subcat_value+=", "+subcategory.name;
            document.getElementById("subcategories").value=subcat_value.substr(2);
        }

    });


}

