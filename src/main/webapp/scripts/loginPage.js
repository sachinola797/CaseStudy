if (role != null && token != null && userId != null)
    window.location = "/";


function submitLoginForm(event) {
    event.preventDefault();
    let user=new FormData();
    user.append("username",document.forms["loginForm"]["username"].value);
    user.append("password",document.forms["loginForm"]["password"].value);
    sendRequest("/login","POST",null,user,function () {
        if(this.status===401)
            document.getElementById("error-msg1").innerHTML="<div class='alert alert-danger'><strong>Username or Password incorrect !!!</strong></div>";
        if(this.status===200){
            localStorage.setItem("userId",this.getResponseHeader("userId"));
            localStorage.setItem("role",this.getResponseHeader("role"));
            localStorage.setItem("Authorization",this.getResponseHeader("Authorization"));
            window.location = "/";
        }
    });
}


function submitSignUpForm() {
    let user= new FormData();
    user.append("name",document.forms["signUpForm"]["name"].value);
    user.append("email",document.forms["signUpForm"]["email"].value);
    user.append("phone",document.forms["signUpForm"]["phone"].value);
    user.append("password",document.forms["signUpForm"]["password"].value);
    sendRequest("/signup","POST",null,user,function () {
        if(this.status===401){
            document.getElementById("error-msg2").innerHTML="<div class='alert alert-danger'><strong>User already exist !!!</strong></div>"
        }
        if(this.status===200){
            alert("Account created successfully !!!");
            window.location="/loginPage";
        }
    });
    return false;
}

