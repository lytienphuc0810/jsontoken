/**
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package net.oauth.jsontoken.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;


public class RsaSHA256Signer extends AbstractSigner {

  private final Signature signature;
  private final PrivateKey signingKey;

  public RsaSHA256Signer(String signerId, String keyId, RSAPrivateKey key) throws InvalidKeyException {
    super(signerId, keyId);

    this.signingKey = key;

    try {
      this.signature = Signature.getInstance("SHA256withRSA");
      this.signature.initSign(signingKey);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("platform is missing RSAwithSHA256 signature alg, or key is invalid", e);
    }
  }

  @Override
  public SignatureAlgorithm getSignatureAlgorithm() {
    return SignatureAlgorithm.RSA_SHA256;
  }

  @Override
  public byte[] sign(byte[] source) throws SignatureException {
    try {
      signature.initSign(signingKey);
    } catch (InvalidKeyException e) {
      throw new RuntimeException("key somehow became invalid since calling the constructor");
    }
    signature.update(source);
    return signature.sign();
  }
}