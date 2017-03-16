$(function() {
    $("#search-box").keyup(debounce(function() { //
            var filter = $.trim($("#search-box").val());

            if (filter) {
                $.ajax(jsRoutes.controllers.HomeController.count(filter))
                    .done(function(data) {
                        console.log(parseInt(data));
                        $('#count').text(data);
                        $('#search-count').show();
                    });
            }
        }, 500)
    );

    $("#table-head-container").mouseleave(function() {
        console.log("hide()");
        $('#search-count').hide();
    });

    $(document).click(function(e) {
        if (!($(e.target).is("#search-box")))
            console.log("hide()2");
            $('#search-count').hide();
    });
});

function debounce(fn, delay) {
    var timer = null;
    return function () {
        var context = this, args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn.apply(context, args);
        }, delay);
    };
}
