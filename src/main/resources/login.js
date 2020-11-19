var Encrypt = {
    key: 'a2ffa5c9be07488bbb04a3a47d3c5f6a',
    iv: '64175472480004614961023454661220',
    nonce: null,
    init: function(){
        var nonce = this.nonceCreat();
        this.nonce = nonce;
        return this.nonce;
    },
    nonceCreat: function(){
        var type = 0;
        var deviceId = '44:f9:71:eb:7b:b7';
        var time = Math.floor(new Date().getTime() / 1000);
        var random = Math.floor(Math.random() * 10000);
        return [type, deviceId, time, random].join('_');
    },
    oldPwd : function(pwd){
        return CryptoJS.SHA1(this.nonce + CryptoJS.SHA1(pwd + this.key).toString()).toString();
    },
    newPwd: function(pwd, newpwd){
        var key = CryptoJS.SHA1(pwd + this.key).toString();
        key = CryptoJS.enc.Hex.parse(key).toString();
        key = key.substr(0, 32);
        key = CryptoJS.enc.Hex.parse(key);
        var password = CryptoJS.SHA1(newpwd + this.key).toString();
        var iv = CryptoJS.enc.Hex.parse(this.iv);
        var aes = CryptoJS.AES.encrypt(
                password,
                key,
                {iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 }
            ).toString();
        return aes;
    }
};
function transform(pwd){
    var nonce = Encrypt.init();
    var oldPwd = Encrypt.oldPwd( pwd );
    var param = {
        username: 'admin',
        password: oldPwd,
        logtype: 2,
        nonce: nonce
    };
    return param
}

function loginHandle ( pwd ) {
    var nonce = Encrypt.init();
    var oldPwd = Encrypt.oldPwd( pwd );
    var param = {
        username: 'admin',
        password: oldPwd,
        logtype: 2,
        nonce: nonce
    };
    $.pub('loading:start');
    var url = '/cgi-bin/luci/api/xqsystem/login';
        $.post( url, param, function( rsp ) {
            $.pub('loading:stop');
            var rsp = $.parseJSON( rsp );
            if ( rsp.code == 0 ) {
                var redirect,
                    token = rsp.token;
                if ( /action=wan/.test(location.href) ) {
                    redirect = buildUrl('wan', token);
                } else if ( /action=lannetset/.test(location.href) ) {
                    redirect = buildUrl('lannetset', token);
                } else {
                    redirect = rsp.url;
                }
                window.location.href = redirect;
            } else if ( rsp.code == 403 ) {
                window.location.reload();
            } else {
                pwdErrorCount ++;
                var errMsg = '密码错误';
                if (pwdErrorCount >= 4) {
                    errMsg = '多次密码错误，将禁止继续尝试';
                }
                Valid.fail( document.getElementById('password'), errMsg, false);
                $( formObj )
                .addClass( 'shake animated' )
                .one( 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
                    $('#password').focus();
                    $( this ).removeClass('shake animated');
                } );
            }
        });
}