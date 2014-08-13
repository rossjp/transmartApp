package com

import org.transmart.searchapp.AuthUser
import org.transmart.searchapp.DataAttestation

import java.text.SimpleDateFormat

class DataAttestationController {

    def springSecurityService

    def index = {
        def user = AuthUser.findByUsername(springSecurityService.getPrincipal().username)
        def da = DataAttestation.findByAuthUserId(user.id)
        if (da == null || !da.hasAgreed())
            render(view:"attestation")
        else
            redirect(uri:'/search');
    }
    def agree = {
        def user = AuthUser.findByUsername(springSecurityService.getPrincipal().username)
        new DataAttestation(authUserId: user.id, lastDateAgreed: new Date()).save()
        redirect(uri: '/search')
    }

    def disagree = {
        redirect(uri: '/logout')
    }
}
