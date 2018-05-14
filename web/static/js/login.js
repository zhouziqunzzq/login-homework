var regUsernameValidate = false;
var regPasswordValidate = false;

var hideAllMsg = function (isFade) {
    if (isFade === true) {
        $(".error-msg").fadeOut();
        $(".msg").fadeOut();
    } else {
        $(".error-msg").hide();
        $(".msg").hide();
    }
};

var showErrorMsg = function (msg) {
    em = $("#error-msg");
    if (em.is(':visible') && msg === em.html()) return;
    hideAllMsg(false);
    em.html(msg);
    em.fadeIn();
};

var showMsg = function (msg) {
    m = $("#msg");
    if (m.is(':visible') && msg === m.html()) return;
    hideAllMsg(false);
    m.html(msg);
    m.fadeIn();
};

var cleanInputStatus = function (input) {
    input.removeClass("text-input-error");
    input.removeClass("text-input-ok");
    input.parent().removeClass("error-x");
    input.parent().removeClass("ok-tick");
};

var setInputStatus = function (input, status) {
    cleanInputStatus(input);
    switch (status) {
        case "error":
            input.addClass("text-input-error");
            input.parent().addClass("error-x");
            break;
        case "ok":
            input.addClass("text-input-ok");
            input.parent().addClass("ok-tick");
            break;
    }
};

var cleanAllRegInputStatus = function () {
    cleanInputStatus($("#register-username"));
    cleanInputStatus($("#register-password"));
    cleanInputStatus($("#register-repassword"));
};

var cleanAllLoginInputStatus = function () {
    cleanInputStatus($("#login-username"));
    cleanInputStatus($("#login-password"));
};

var checkRegUsername = function () {
    var uname = $("#register-username").val();
    if (uname === "") {
        setInputStatus($("#register-username"), "error");
        showErrorMsg("请输入用户名");
        return;
    }
    fetch("/auth/register/check-username", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: $.param({
            username: uname
        })
    }).then(function (rst) {
        if (rst.ok) {
            rst.json()
                .then(function (rst) {
                    if (rst.result === true) {
                        setInputStatus($("#register-username"), "ok");
                        showMsg(rst.msg);
                        regUsernameValidate = true;
                    } else {
                        setInputStatus($("#register-username"), "error");
                        showErrorMsg(rst.msg);
                        regUsernameValidate = false;
                    }
                })
        }
    });
};

var checkRegPassword = function () {
    inputPwd = $("#register-password");
    inputRepwd = $("#register-repassword");
    pwd = inputPwd.val();
    repwd = inputRepwd.val();
    if (repwd === "") return;
    if (pwd === "" || pwd !== repwd) {
        setInputStatus(inputPwd, "error");
        setInputStatus(inputRepwd, "error");
        showErrorMsg("两次输入的密码不一致");
        regPasswordValidate = false;
    } else {
        setInputStatus(inputPwd, "ok");
        setInputStatus(inputRepwd, "ok");
        showMsg("密码输入正确");
        regPasswordValidate = true;
    }
};

var postRegister = function () {
    if (!(regPasswordValidate && regUsernameValidate)) {
        return;
    }
    var username = $("#register-username").val();
    var password = $("#register-password").val();
    var repassword = $("#register-repassword").val();
    if (username === "") {
        setInputStatus($("#register-username"), "error");
        showErrorMsg("请输入用户名");
        return;
    }
    fetch("/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: $.param({
            username: username,
            password: password,
            repassword: repassword
        })
    }).then(function (rst) {
        if (rst.ok) {
            rst.json()
                .then(function (rst) {
                    if (rst.result === true) {
                        cleanAllRegInputStatus();
                        switchChooser("login");
                        showMsg(rst.msg);
                    } else {
                        showErrorMsg(rst.msg);
                    }
                })
        }
    });
};

var postLogin = function () {
    var username = $("#login-username").val();
    var password = $("#login-password").val();
    if (username === "") {
        setInputStatus($("#login-username"), "error");
        showErrorMsg("请输入用户名");
        return;
    }
    if (password === "") {
        setInputStatus($("#login-password"), "error");
        showErrorMsg("请输入密码");
        return;
    }
    cleanAllLoginInputStatus();
    fetch("/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: $.param({
            username: username,
            password: password,
        })
    }).then(function (rst) {
        if (rst.ok) {
            rst.json()
                .then(function (rst) {
                    if (rst.result === true) {
                        showMsg(rst.msg);
                        var welcome = $("#welcome-msg");
                        welcome.html("欢迎您，" + username + "！");
                        $("#login").hide();
                        welcome.fadeIn();
                    } else {
                        switch (rst.msg) {
                            case "用户名不存在！":
                                setInputStatus($("#login-username"), "error");
                                break;
                            case "密码错误！":
                                setInputStatus($("#login-password"), "error");
                                break;
                        }
                        showErrorMsg(rst.msg);
                    }
                })
        }
    });
};

var switchChooser = function (section) {
    var loginChooser = $("#login-chooser");
    var registerChooser = $("#register-chooser");
    var loginForm = $("#login");
    var registerForm = $("#register");
    switch (section) {
        case "login":
            loginChooser.addClass('chooser-active');
            registerChooser.removeClass('chooser-active');
            loginForm.fadeIn();
            registerForm.hide();
            break;
        case "register":
            loginChooser.removeClass('chooser-active');
            registerChooser.addClass('chooser-active');
            loginForm.hide();
            registerForm.fadeIn();
    }
};

$(document).ready(function () {
    // Auto clear ok or error status
    $("input").bind('click', function () {
        this.classList.remove("text-input-ok");
        this.classList.remove("text-input-error");
    });
    $(".input-wrapper").bind('click', function () {
        this.classList.remove("error-x");
        this.classList.remove("ok-tick");
    });
    // Init chooser
    switchChooser("login");
    $("#login-chooser").click(function () {
        switchChooser("login");
        hideAllMsg(true);
    });
    $("#register-chooser").click(function () {
        switchChooser("register");
        hideAllMsg(true);
    });
    // Init register
    $("#register-username").blur(function () {
        checkRegUsername();
    });
    $("#register-password").bind('input propertychange', function () {
        checkRegPassword();
    });
    $("#register-repassword").bind('input propertychange', function () {
        checkRegPassword();
    });
    $("#register-submit").click(function (e) {
        e.preventDefault();
        postRegister();
    });
    // Init login
    $("#login-submit").click(function (e) {
        e.preventDefault();
        postLogin();
    })
});