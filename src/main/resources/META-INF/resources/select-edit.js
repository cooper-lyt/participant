$(document).ready(
    function(){
        $('.select-edit-input').on("focus",
            function (){
                $(this).dropdown();
            }
        )


        $('.select-edit-group').on('hide.bs.dropdown', function () {
            if ( $(this).children("input").is(':focus')){
                return false;
            }
        })

        $('.select-edit-group>.dropdown-menu>li>a').on("click",
            function (e){
                $(this).parents(".select-edit-group").children("input").val($(this).text());
                e.preventDefault();

            }
        )

    }
)