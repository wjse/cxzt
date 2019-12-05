function api(path , type , data , success , headers){
    let options = {
        url : path,
        dataType : "json",
        contentType : "application/json",
        type : type,
        beforeSend: function(request) {
            let token = window.sessionStorage.getItem("token");
            if(token){
                request.setRequestHeader("Token" , token);
            }
            if(headers){
                for(let k in headers){
                    request.setRequestHeader(k , headers[k]);
                }
            }
        },
        success : function(res , status , response){
            let token = response.getResponseHeader("Token");
            if(token){
                window.sessionStorage.setItem("token" , token);
            }
            success.call(this , res);
        },
        error : function(e){
            if(e.status == 401){
                window.location.href = "/login.html"
            }
        }
    };

    if(data){
        options.data = JSON.stringify(data);
    }
    $.ajax(options);
};