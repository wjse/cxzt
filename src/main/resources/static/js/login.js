(function(){
    $("#login").on("click" , () => {
        $("#form").on("submit" , () => false)
        let username = $("#username").val() , password = $("#password").val();
        if(!username || !password){
            return;
        }
        password = sha1(password);
        api("/login" , "post" , {username : username , password : password , from : "web"} , function(res){
            if(res.code == 200){
                window.sessionStorage.setItem("token" , res.token);
                window.sessionStorage.setItem("username" , res.username);
                window.location.href = "/index.html";
            }else{
                $("#login-error").text(res.message).show();
            }
        });
    });
})($);