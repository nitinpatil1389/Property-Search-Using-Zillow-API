$('#mainTab a').click(function (e) {
alert("hi");
	e.preventDefault();
	$(this).tab('show');
});

$('#myTab a').click(function (e) {

	$('.carousel').carousel({interval: 2000})
});