from django import forms


class OslerIdForm(forms.Form):
    current = forms.CharField()


class OslerIdNextForm(forms.Form):
    next = forms.IntegerField(initial=1)


