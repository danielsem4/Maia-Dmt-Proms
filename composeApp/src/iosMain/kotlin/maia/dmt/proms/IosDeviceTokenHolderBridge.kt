package maia.dmt.proms

import maia.dmt.home.data.notificaiton.IosDeviceTokenHolder

object IosDeviceTokenHolderBridge {

    fun updateToken(token: String?) {
        IosDeviceTokenHolder.updateToken(token)
    }

}