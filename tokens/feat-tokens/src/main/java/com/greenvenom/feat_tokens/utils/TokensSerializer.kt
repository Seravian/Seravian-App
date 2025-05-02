package com.greenvenom.feat_tokens.utils

import androidx.datastore.core.Serializer
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.crypto.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

object TokensSerializer : Serializer<Tokens> {
    private const val KEY_ALIAS = "Token"

    override val defaultValue: Tokens
        get() = Tokens()

    override suspend fun readFrom(input: InputStream): Tokens {
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

    override suspend fun writeTo(t: Tokens, output: OutputStream) {
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