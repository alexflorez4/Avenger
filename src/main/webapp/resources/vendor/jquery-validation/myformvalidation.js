$(document).ready(function() {
    $('#orderForm').validate( {
        rules: {
            reference: "required",
            itemQuantity: "required",
            bName: {
                required:true,
                minlength: 2
            },
            bAddress: "required",
            bCiti: "required",
            bState: "required",
            bCountry: "required",
            bEmail: {
             email: true,
             required: true
             }
        },
        messages: {
            reference: "Please select a reference",
            itemQuantity: "Please enter quantity",
            bName:{
                required: "Please enter buyer name",
                minlength: "Buyer name must have at least 2 characters"
            },
            bAddress: "Please enter buyer address",
            bCiti: "Please enter buyer city",
            bState: "Please enter State",
            bCountry: "Please select a country",
            bEmail: "Please enter a valid email address"
        },
        errorElement: "em",
        errorPlacement: function ( error, element ) {
            // Add the `help-block` class to the error element
            error.addClass( "help-block" );
            // Add `has-feedback` class to the parent div.form-group
            // in order to add icons to inputs
            element.parents( ".col-sm-5" ).addClass( "has-feedback" );
            if ( element.prop( "type" ) === "checkbox" ) {
                error.insertAfter( element.parent( "label" ) );
            } else {
                error.insertAfter( element );
            }
            // Add the span element, if doesn't exists, and apply the icon classes to it.
            if ( !element.next( "span" )[ 0 ] ) {
                $( "<span class='glyphicon glyphicon-remove form-control-feedback'></span>" ).insertAfter( element );
            }
        },
        success: function ( label, element ) {
            // Add the span element, if doesn't exists, and apply the icon classes to it.
            if ( !$( element ).next( "span" )[ 0 ] ) {
                $( "<span class='glyphicon glyphicon-ok form-control-feedback'></span>" ).insertAfter( $( element ) );
            }
        },
        highlight: function ( element, errorClass, validClass ) {
            $( element ).parents( ".col-sm-5" ).addClass( "has-error" ).removeClass( "has-success" );
            $( element ).next( "span" ).addClass( "glyphicon-remove" ).removeClass( "glyphicon-ok" );
        },
        unhighlight: function ( element, errorClass, validClass ) {
            $( element ).parents( ".col-sm-5" ).addClass( "has-success" ).removeClass( "has-error" );
            $( element ).next( "span" ).addClass( "glyphicon-ok" ).removeClass( "glyphicon-remove" );
        }
    } );
});