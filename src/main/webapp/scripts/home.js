function category_dropdown() {

    sendRequest("/products/allCategories", "GET", null, null, function() {
        if (this.status === 200) {
            const categories = JSON.parse(this.responseText);
            let select_option = document.getElementById("cat_choice");
            for (let cat of categories)
                select_option.innerHTML += "<option name='category' value='" + cat.name + "' id='C" + cat.categoryId + "_" + cat.name + "'>" + cat.name + "</option>";
        }

    });
}

//---------------------------------------Navbar ends--------------------------------------------------------------------------------------------


//----------------------------------------- Filters -----------------------------------------------------------------------------

function categoryFilterFillUp() {
    sendRequest("/products/allCategories", "GET", null, null, function() {
        if (this.status === 200) {
            const allcategories = JSON.parse(this.responseText);
            let categoryList = document.getElementById("category");
            for (let categoryData of allcategories) {
                let category_div = document.createElement("div");
                category_div.classList.add("card");
                let category_header = document.getElementById("category_header").cloneNode(true);
                category_header.children[0].href = "#C" + categoryData.categoryId;
                category_header.children[0].innerHTML = categoryData.name;
                category_div.appendChild(category_header);
                let category_body = document.getElementById("category_body_id").cloneNode(true);
                category_body.id = "C" + categoryData.categoryId;
                let sortedSubcategories=categoryData.subcategories.sort(function(a,b) {
                                                            var x = a.name.toLowerCase();
                                                            var y = b.name.toLowerCase();
                                                            return x < y ? -1 : x > y ? 1 : 0;
                                                        });
                for (let subcategoryData of sortedSubcategories) {
                    let subcategory_element = category_body.children[0].children[0].cloneNode(true);
                    subcategory_element.classList.remove("d-none");
                    subcategory_element.children[0].htmlFor = "C" + categoryData.categoryId + "_" + subcategoryData.subcategoryId;
                    subcategory_element.children[0].children[0].id = "C" + categoryData.categoryId + "_" + subcategoryData.subcategoryId;
                    subcategory_element.children[0].children[0].value = subcategoryData.name;
                    subcategory_element.children[0].innerHTML += subcategoryData.name;

                    category_body.children[0].appendChild(subcategory_element);
                }
                category_div.appendChild(category_body);
                categoryList.appendChild(category_div);
            }
        }
    })
}

// if you select a category from filters corresponding category will be reflected a search bar
function selectCategory(Element) {
    //for local host: substr(23)
    //for ip : substr(26)
    document.getElementById(Element.children[0].href.substr(26) + "_" + Element.children[0].innerHTML).selected = "selected";
}


// if you select a category from search corresponding category will be opens on filters
function selectFilterCategory(SelectElement) {
    const allCat = document.getElementsByName("category_filter_list");
    for (const cat of allCat)
        cat.classList.remove("show");

    let id = SelectElement.options[SelectElement.selectedIndex].id.split("_")[0];
    let filterElement = document.getElementById(id);
    if (filterElement != null)
        filterElement.classList.add("show");
}

// display modify or add to cart button on products
function checkForAdmin(Element) {
    let buttons = Element.parentElement.children[1].children[4];
    if (role === "ADMIN") {
        buttons.children[1].classList.add("d-none");
        buttons.children[2].classList.remove("d-none");
    }
}

function Filters(subcategories, searchString, minPrice, maxPrice) {
    this.subcategories = subcategories;
    this.searchString = searchString;
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
}


// function for filters search
function searchProductsWithFilters() {
    const cat_choice = document.getElementById("cat_choice");
    const selectedCategory = cat_choice.options[cat_choice.selectedIndex];
    const categoryName = selectedCategory.value;


    const searchString = document.getElementById("searchString").value;

    const categoryId = selectedCategory.id.split("_")[0];
    const filterCategoryElement = document.getElementById(categoryId);
    const subcategoryData = new FormData(filterCategoryElement.children[0]);
    const selectedSubcategories = subcategoryData.getAll("subcategory"); //gets all the selected subcategories


    const minPrice = document.getElementById("minPrice").value;


    const maxPrice = document.getElementById("maxPrice").value;

    let filterList = new Filters(selectedSubcategories, searchString, minPrice, maxPrice);


    let header = new Header("Content-Type", "application/json;charset=UTF-8");

    sendRequest("/products/" + categoryName + "/getFilteredProducts", "POST", [header], JSON.stringify(filterList), setProducts);


}

// loads products at home initially
function loadProducts() {
    sendRequest("/products/allProducts", "GET", null, null, setProducts);
}

// function for displaying all result products
let setProducts = function() {
    if (this.status === 200) {
        let productDataList = JSON.parse(this.response);


        let product_display = document.getElementById("product_display");
        product_display.innerHTML = "";

        if (productDataList.length === 0) {
            product_display.innerHTML = "<div class='alert alert-danger mt-5'><h1 class='display-2 p-2'><strong>No result found!!!</strong></h1></div>"
            return;
        }

        for (let productData of productDataList) {
            let product = document.getElementById("product_Template").cloneNode(true);
            product.classList.remove("d-none");
            product.children[1].id = "P_" + productData.productId;
            product.children[1].children[0].innerHTML = productData.name;
            product.children[1].children[1].innerHTML = productData.category;
            let subcategories = "";
            for (let s of productData.subcategories)
                subcategories += ", " + s.name;
            product.children[1].children[2].children[0].innerHTML = subcategories.substr(2);
            product.children[1].children[3].innerHTML = productData.details;
            product.children[1].children[4].children[0].innerHTML= "₹ " + productData.price;
            product_display.appendChild(product);
        }
    }
};

// for admin to modify a selected product
function modifyProduct(Obj) {
    let productId = Obj.parentElement.parentElement.id.split("_")[1];
    localStorage.setItem("modifyProduct_Id", productId);
    window.location = "/modifyProductPage";
}