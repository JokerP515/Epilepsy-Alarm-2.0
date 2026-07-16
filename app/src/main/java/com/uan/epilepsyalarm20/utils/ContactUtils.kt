package com.uan.epilepsyalarm20.utils

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

fun getContactFromUri(
    context: Context,
    uri: Uri
): Pair<String, String>? {

    val resolver = context.contentResolver

    val cursor = resolver.query(
        uri,
        null,
        null,
        null,
        null
    ) ?: return null

    cursor.use {

        if (!it.moveToFirst()) return null

        val id = it.getString(
            it.getColumnIndexOrThrow(
                ContactsContract.Contacts._ID
            )
        )

        val name = it.getString(
            it.getColumnIndexOrThrow(
                ContactsContract.Contacts.DISPLAY_NAME
            )
        )

        var phone = ""

        val phoneCursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(id),
            null
        )

        phoneCursor?.use { phones ->

            if (phones.moveToFirst()) {

                phone = phones.getString(
                    phones.getColumnIndexOrThrow(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                )
            }
        }

        return Pair(name, cleanPhoneNumber(phone))
    }
}

fun cleanPhoneNumber(phone: String): String {
    val hasPlus = phone.trim().startsWith("+")

    val digits = phone.filter { it.isDigit() }

    return if (hasPlus) "+$digits" else digits
}