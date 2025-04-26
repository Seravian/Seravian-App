package com.greenvenom.feat_tokens.utils

import androidx.datastore.core.Serializer
import com.greenvenom.crypto.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

object TokensSerializer : Serializer<com.greenvenom.core_tokens.domain.Tokens> {
    private const val KEY_ALIAS = "Token"

    override val defaultValue: com.greenvenom.core_tokens.domain.Tokens
        get() = com.greenvenom.core_tokens.domain.Tokens(
            accessToken = "",
            refreshToken = "",
            accessExpiresIn = ""
        )

    override suspend fun readFrom(input: InputStream): com.greenvenom.core_tokens.domain.Tokens {
        val encryptedBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }
        val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
        val decryptedBytes = Crypto.decrypt(
            keyAlias = KEY_ALIAS,
            bytes = encryptedBytesDecoded
        )
        val decodedJsonString = decryptedBytes.decodeToString()
        return Json.decodeFromString(decodedJsonString)
    }

    override suspend fun writeTo(t: com.greenvenom.core_tokens.domain.Tokens, output: OutputStream) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = Crypto.encrypt(
            keyAlias = KEY_ALIAS,
            bytes = bytes
        )
        val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedBytesBase64)
            }
        }
    }
}