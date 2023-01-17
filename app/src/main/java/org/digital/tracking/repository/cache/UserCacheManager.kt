package org.digital.tracking.repository.cache

import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.model.DeviceListItem
import org.digital.tracking.model.User
import org.digital.tracking.repository.VehicleRepository


object UserCacheManager {

    private var user: User? = null
    private var deviceList: List<DeviceListItem> = ArrayList()
    private var imeiList: ArrayList<String> = ArrayList()
    var fallbackImei: String = ""

    fun setUser(user: User) {
        UserCacheManager.user = user
        setDeviceList(user.deviceList)
        user.deviceList?.forEach {
            imeiList.add(it.imei)
            return@forEach
        }
    }

    fun setUser(user: User, sharedPrefs: SharedPrefs) {
        setUser(user)
        user.deviceList?.forEach {
            fallbackImei = it.imei
            sharedPrefs.fallbackImei = it.imei
            return@forEach
        }
    }

    fun getUser(): User? {
        return user
    }

    fun setDeviceList(list: List<DeviceListItem>?) {
        if (list == null) {
            return
        }
        deviceList = list
    }

    fun getDeviceList(): List<DeviceListItem> {
        return deviceList
    }

    fun getDeviceFromImei(deviceImei: String): DeviceListItem? {
        return deviceList.find { it.imei == deviceImei }
    }

    fun getDeviceImeiList(): List<String> {
        return imeiList
    }

    fun destroy() {
        user = null
        deviceList = ArrayList()
        imeiList.clear()
        fallbackImei = ""
    }

    fun removeUserSession(sharedPrefs: SharedPrefs) {
        destroy()
        VehicleRepository.destroy()
        sharedPrefs.clearAllKeys()
        sharedPrefs.isUserFirstTime = false
    }


}