if (role == null || token == null || role !== "USER" || userId == null)
    window.location = "/loginPage";


function setDetails() {
    let header = new Header("Authorization",token);
    sendRequest("http://localhost:8080/getProfile/" + userId, "GET", [header], null, insertDetails);
}

let insertDetails = function() {

    if (this.status === 401 || this.getResponseHeader("tokenValidity") === "expired"){
        localStorage.removeItem("Authorization");
        localStorage.removeItem("userId");
        localStorage.removeItem("role");
        window.location = "/loginPage";
    }

    if (this.status === 200) {
        let userData = JSON.parse(this.responseText);
        let userInfo = document.getElementById("user-info");
        userInfo.children[0].children[0].innerHTML += userData.name;
        userInfo.children[1].children[0].innerHTML += userData.email;

        document.getElementById("name").value = userData.name;
        document.getElementById("email").defaultValue = userData.email;


        if (userData.address == null) {
            userInfo.children[2].children[0].innerHTML += "Not provided yet !!!";
            userInfo.children[3].children[0].innerHTML = "<div class='alert alert-danger'><strong>Address is not provided yet !!!</strong><br>Please Update the your address...</div>";
            document.getElementById("edit-button").style.color = "red";

        }
        else {
            userInfo.children[2].children[0].innerHTML += userData.phone;
            userInfo.children[3].children[0].children[0].children[0].innerHTML += userData.address.street;
            userInfo.children[3].children[0].children[1].children[0].innerHTML += userData.address.city;
            userInfo.children[3].children[0].children[2].children[0].innerHTML += userData.address.state;
            userInfo.children[3].children[0].children[3].children[0].innerHTML += userData.address.pincode;

            document.getElementById("phone").value = userData.phone;
            document.getElementById("street").value = userData.address.street;
            document.getElementById("city").value = userData.address.city;
            document.getElementById("state").value = userData.address.state;
            document.getElementById("pincode").value = userData.address.pincode;
        }
    }
};


function UserProfile(userID, name, email,phone,address) {
    this.userID = userID;
    this.email=email;
    this.name = name;
    this.phone = phone;
    this.address=address;

}
function Address(street, city, state, pincode) {
    this.street = street;
    this.city = city;
    this.state = state;
    this.pincode = pincode;
}

function submitUserProfileForm() {
    event.preventDefault();
    const address=new Address(document.getElementById("street").value, document.getElementById("city").value,
        document.getElementById("state").value, document.getElementById("pincode").value);

    let user = new UserProfile(userId, document.getElementById("name").value, document.getElementById("email").value, document.getElementById("phone").value,address);
    let header1 = new Header("Authorization", token);
    let header2 = new Header("Content-Type", "application/json;charset=UTF-8");
    sendRequest("http://localhost:8080/updateProfile", "POST", [header1, header2], JSON.stringify(user), function() {

        if (this.status===401 || this.getResponseHeader("tokenValidity") === "expired"){
            localStorage.removeItem("Authorization");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");
            window.location = "/loginPage";
        }
        if(this.status===400){
            document.getElementById("error-msg1").innerHTML="<div class='alert alert-danger'><strong>"+this.responseText+"</strong></div>";
            alert(this.responseText);
        }
        if(this.status===200){
            alert(this.responseText);
            if(document.getElementById("email").defaultValue!==document.getElementById("email").value) {
                alert("You have updated your \"Email Id\",so please login again!!!");
                localStorage.removeItem("Authorization");
                localStorage.removeItem("userId");
                localStorage.removeItem("role");
            }
            window.location="userProfilePage.html";
        }
        return false;
    });
}